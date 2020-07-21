package com.custom.rac.datamanagement.importer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.custom.rac.datamanagement.action.ImportAction2;
import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.PropertyContainer;
import com.custom.rac.datamanagement.util.XMLResult;
import com.custom.rac.itemcode.util.GetItemIDFactory;
import com.sfgk.sie.customs.part.rendering.MPartRendering;
import com.sfgk.sie.customs.util.GetPartItemID;
import com.sfgk.sie.webservice.SFGKServiceProxy;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCComponentFolderType;
import com.teamcenter.rac.kernel.TCComponentICO;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;

public class SFGKNewPartsImporter extends AbstractImporter {

	private static HashMap<String, String> typeMap = new HashMap<String, String>();
	private TCSession session = (TCSession) AIFUtility.getDefaultSession();
	private TCComponentItemType itemType = null;
	private TCComponentFolder folder = null;
	private TCComponentItemRevision rev = null;
	protected int folderChildIndex = 0;
	private MPartRendering rendering;
	private String tempDesc = null;
	private String org = null;
	private String template = null;
	private String uom_tag = null;
	private String tempSameId = null;
	SFGKServiceProxy proxy = new SFGKServiceProxy();

	static {	
		typeMap.put("123", "SF8_PPart");
		typeMap.put("122", "SF8_SPart");
		typeMap.put("124", "SF8_WPart");
		typeMap.put("121", "SF8_RPart");
		typeMap.put("125", "SF8_FPart");
		typeMap.put("126", "SF8_BPart");
	}

	@Override
	public String getName() {

		return "上风高科新物料批量创建程序";

	}

	@Override
	public TCComponentItemType getItemType(int index) throws Exception {
		String icsCode = (String) getValue(index, "物料分类ID");
		String reltype = null;
		if (icsCode.isEmpty()) {
			throw new Exception("物料分类ID不能为空");
		} else {
			String key = icsCode.substring(0, 3);
			reltype = typeMap.get(key);
			try {
				itemType = (TCComponentItemType) session.getTypeComponent(reltype);
			} catch (TCException e) {
				e.printStackTrace();
			}
			if (itemType == null) {
				throw new Exception("物料类型不存在");
			}
		}
		return itemType;
	}

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSetPropertyError(int index, String propertyDisplayName, Exception e) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public PropertyContainer getPropertyContainer(int index) throws Exception {

		return PropertyContainer.itemRevision;

	}

	@Override
	public void onSingleStart(int index) throws Exception {
//		Map<String, String> map = values.get(index);
//		String icsCode = map.get("物料分类ID");
//		throw new Exception("物料分类ID有误，无法导入！");
	}

	@Override
	public void onSingleFinish(int index, TCComponent newInstance) throws Exception {
		Map<String, String> map = values.get(index);
		org = map.get("组织");
		template = map.get("用户模板");
		uom_tag = map.get("度量单位");
		rev = (TCComponentItemRevision) newInstance;
//		tempDesc = rendering.autoBuildValue().substring(5);
		rendering = new MPartRendering(rev, false, false);

		tempDesc = rendering.autoBuildProperties.get("描述").synx();

		tempDesc.trim();
		rev.setProperty("object_desc", tempDesc);
		rev.setProperty("sf8_create_part_template", org + "-" + template);
		rev.getItem().setProperty("uom_tag", uom_tag);
		rev.getItem().refresh();
		rev.refresh();
		if (rendering.hasObjectDescAndDetailDescButNotThisItem()) {
			rev.getItem().delete();
			driver.onNewItemRevDesc(index, tempDesc);
			driver.onNewItemId(index, "");
			String detailDesc = rev.getProperty("sf8_detail_desc");
			tempSameId = findSameItem(tempDesc, detailDesc);
			throw new Exception("系统存在相同描述的物料:创建失败" + tempSameId);
		} else {
			driver.onNewItemRevDesc(index, tempDesc);
			folder.add("contents", rev.getItem());
		}
	}

	/**
	 * 查找相同描述的物料号
	 * 
	 * @param tempDesc
	 * @return
	 * @throws Exception
	 */
	public String findSameItem(String tempDesc, String detailDesc) throws Exception {
		String tempid = "";
		List<String> tempSameItemList = new ArrayList<String>();
		TCComponent[] result = null;
		if (detailDesc.equals("")) {
			result = session.search("SF8_SearchDescFromPart", new String[] { "描述" }, new String[] { tempDesc });
		} else {
			result = session.search("SF8_SearchDescFromPart", new String[] { "描述", "详细说明" },
					new String[] { tempDesc, detailDesc });
		}
		if (result.length > 0) {
			for (int i = 0; i < result.length; i++) {
				tempSameItemList.add(result[i].getProperty("item_id"));
			}
			if (tempSameItemList.size() > 0) {
				HashSet<String> h = new HashSet<String>(tempSameItemList);
				tempSameItemList.clear();
				tempSameItemList.addAll(h);
				for (int i = 0; i < tempSameItemList.size(); i++) {
					tempid = tempid + tempSameItemList.get(i) + ",";
				}
				if (tempid.contains(",")) {
					tempid = tempid.substring(0, tempid.length() - 1);
				}
			}
		}
		return tempid;
	}

	public Object getValue(int index, String propertyDisplayName) {
		Object obj = super.getValue(index, propertyDisplayName);
		if (obj != null && obj instanceof String) {
			return ((String) obj).replaceAll("\r|\n", "").replaceAll("\\*|×", "x");
		} else {
			return obj;
		}
	}

	public String getValueFromRealName(int index, String realName) throws Exception {
		String str = super.getValueFromRealName(index, realName);
		return str == null ? null : str.replaceAll("\r|\n", "").replaceAll("\\*|×", "x");
	}

	@Override
	public void onSingleError(int index, Exception e) throws Exception {

	}

	@Override
	public void onStart() throws Exception {
		System.out.println("导入开始..");
		if (checkProperties()) {
			System.out.println("必要属性检查通过");
			TCComponentFolderType folderType = (TCComponentFolderType) session.getTypeComponent("Folder");
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String name = "批量新建物料" + format.format(date);
			folder = folderType.create(name, "", "Folder");
			session.getUser().getHomeFolder().add("contents", folder);
		} else {
			throw new Exception("必要属性检查不通过，请检查7个（代码）属性和度量单位");
		}
	}

	static ArrayList<String> ignoreList = new ArrayList<String>();
	static {
		ignoreList.add("序号");
		ignoreList.add("物料号");
		ignoreList.add("名称");
		ignoreList.add("型号(代码)");
		ignoreList.add("机号(代码)");
		ignoreList.add("传动方式(代码)");
		ignoreList.add("零部件(代码)");
		ignoreList.add("品牌(代码)");
		ignoreList.add("极数功率(代码)");
		ignoreList.add("系列(代码)");
		ignoreList.add("物料分类ID");
		ignoreList.add("度量单位");
		ignoreList.add("组织");
		ignoreList.add("用户模板");
	}

	@Override
	public boolean ignoreProperty(int index, String propertyDisplayName) {
		return ignoreList.contains(propertyDisplayName);
	}

	@Override
	public boolean ignoreRow(int index) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteOldItemWhenItemIdExist(int index) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onSingleMessage(int index, String msg) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPropertyRealNameNotFound(int index, String propertyName) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 检查编码字段及度量单位
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean checkProperties() throws Exception {

		Map<String, String> nesPriopertiesMap = values.get(0);
		boolean hasmodelcode = nesPriopertiesMap.containsKey("型号(代码)");
		boolean hasmachinecode = nesPriopertiesMap.containsKey("机号(代码)");
		boolean hasdrivecode = nesPriopertiesMap.containsKey("传动方式(代码)");
		boolean haspartname = nesPriopertiesMap.containsKey("零部件(代码)");
		boolean hasbrandname = nesPriopertiesMap.containsKey("品牌(代码)");
		boolean hasserive = nesPriopertiesMap.containsKey("系列(代码)");
		boolean haspole = nesPriopertiesMap.containsKey("极数功率(代码)");
		boolean hasItemUom = nesPriopertiesMap.containsKey("度量单位");
		if (hasmodelcode && hasItemUom && hasmachinecode && hasdrivecode && haspartname && hasbrandname && hasserive
				&& haspole) {
			return true;
		}
		return false;
	}

	@Override
	public void execute() throws Exception {

		onStart();
		driver.onStart();
		TCComponent newInstance = null;
		String icsCode = null;
		for (int i = 0; i < values.size(); i++) {
			// 如果单击停止按钮，会停止改程序的运行
			if (ImportAction2.getThread().getRunState().equals("停止"))
				break;
			// 如果单击暂停按钮，程序会进行休眠
			if (ImportAction2.getThread().getRunState().equals("暂停")) {
				ImportAction2.o.wait();
			}
			if (ignoreRow(i))
				continue;
			onSingleStart(i);
			driver.onSingleStart(i);
			Map<String, String> map = values.get(i);
			icsCode = map.get("物料分类ID");
			if (icsCode.equals("126")) {
				throw new Exception("126分类ID错误");
			}
			try {
				newInstance = newTCComponent(i);
				addClassification(newInstance, icsCode);

				for (String propertyDisplayName : map.keySet()) {
					try {
						if (ignoreProperty(i, propertyDisplayName))
							continue;
						setValue(newInstance, i, propertyDisplayName);
						onSetPropertyFinish(i, propertyDisplayName);
						driver.onSetPropertyFinish(i, propertyDisplayName);
					} catch (Exception e) {
						onSetPropertyError(i, propertyDisplayName, e);
						driver.onSetPropertyError(i, propertyDisplayName, e);
						if (newInstance instanceof TCComponentItemRevision) {
							((TCComponentItemRevision) newInstance).getItem().delete();
						}

						throw e;
					}
				}
				onSingleFinish(i, newInstance);
				driver.onSingleFinish(i);
			} catch (Exception e) {
				// TODO 异常处理
				e.printStackTrace();
				onSingleError(i, e);
				driver.onSingleError(i, e);
			}
		}
		onFinish();
	}

	/**
	 * -创建新实例
	 * 
	 * @param index
	 * @return
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings("deprecation")
	public TCComponent newTCComponent(int index) throws Exception {

		TCComponentItemType itemType = getItemType(index);
		String temp = null;
		String prefix = null;
		String serlenth = null;
		String id = null;

		TCComponentItem item = null;

		Map<String, String> map = values.get(index);
		temp = getItemPrefixandSerialLength(map);
		prefix = temp.split("&")[0];
		serlenth = temp.split("&")[1];
		id = GetPartItemID.getCurrentID(prefix, Integer.parseInt(serlenth));
		System.out.println("");
		String itemId = id;
		String itemRev = "A";
		if (id.length() != 14) {
			throw new Exception("获取的编码不等以14位。获取物料ID出错，无法创建物料。");
		}

		if (itemId == null || itemId.length() == 0) {
			itemId = newItemId(index);
			driver.onNewItemId(index, itemId);
		} else {
			driver.onNewItemId(index, itemId);
			item = itemType.find(itemId);
			if (item != null && deleteOldItemWhenItemIdExist(index)) {
				itemUtil.deleteItem(item, true);
				item = null;
			}
		}
		if (itemRev == null || itemRev.length() == 0) {
			itemRev = newItemRevId(index);
			driver.onNewItemRevId(index, itemRev);
		}
		TCComponentItem newItem = item != null ? item
				: itemType.create(itemId, itemRev, itemType.getTypeName(), getItemObjectName(index), null, null, null,
						null);
//		newItem.setLogicalProperty("sf8_if_history_data", true);
		return getPropertyContainer(index) == PropertyContainer.item ? newItem : newItem.getLatestItemRevision();
	}

	/**
	 * 获取前缀和流水
	 * 
	 * @param map
	 * @return
	 */
	public String getItemPrefixandSerialLength(Map<String, String> map) {
		String code = null;
		code = GetItemIDFactory.getID(map);
		return code;
	}

	/**
	 * 获取物料编码
	 * 
	 * @param prefix
	 * @param serialLength
	 * @return
	 * @throws Exception
	 */
	public String getID(String prefix, int serialLength) throws Exception {
		SFGKServiceProxy proxy = new SFGKServiceProxy();
		String xml = proxy.getID(prefix, serialLength);
		XMLResult result = XMLResult.read(xml);
		String error = result.error;
		if (error != null && !error.isEmpty()) {
			throw new Exception(error);
		}
		return result.value;
	}

	/**
	 * 发送到分类
	 * 
	 * @param tcc
	 * @param ics_id
	 * @throws Exception
	 */
	public void addClassification(TCComponent tcc, String ics_id) throws Exception {

		TCComponentICO[] icos = tcc.getClassificationObjects();
		if (icos == null || icos.length == 0) {
			SFGKServiceProxy proxy = new SFGKServiceProxy();
			String msg = proxy.classify(tcc.getUid(), ics_id);
			if (msg != null) {
				throw new Exception(msg);
			}
		}
	}

}
