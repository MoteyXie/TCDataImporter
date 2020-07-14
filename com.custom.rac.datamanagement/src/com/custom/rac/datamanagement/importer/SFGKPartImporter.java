package com.custom.rac.datamanagement.importer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.MyClassifyManager;
import com.custom.rac.datamanagement.util.MyStatusUtil;
import com.custom.rac.datamanagement.util.PropertyContainer;
import com.custom.rac.itemcode.util.GetIcsCodeByItemCode;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCComponentFolderType;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCComponentType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCPropertyDescriptor;
import com.teamcenter.rac.kernel.TCSession;

public class SFGKPartImporter extends AbstractImporter {

	private static HashMap<String, String> typeMap = new HashMap<String, String>();
	private TCSession session = (TCSession) AIFUtility.getDefaultSession();
	private MyClassifyManager cls_manger = new MyClassifyManager(session);
	private TCComponentItemType itemType = null;
	private TCComponentFolder folder = null;
	private TCComponentItemRevision rev = null;
	protected int folderChildIndex = 0;

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
		return "上风高科物料导入程序";
	}

	@Override
	public void execute() throws Exception {
		super.execute();
	}

	@Override
	public TCComponentItemType getItemType(int index) throws Exception {
		String icsCode = (String) getValue(index, "物料号");
		String reltype = null;
		if (icsCode.isEmpty()) {
			throw new Exception("物料号不能为空");
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
	public void setValue(TCComponent tcComponent, int index, String propertyDisplayName) throws Exception {
		String value = getValue(index, propertyDisplayName) + "";
		String id = tcComponent.getProperty("item_id");
		if (propertyDisplayName.equals("物料状态")) {
			MyStatusUtil.setStatus(tcComponent, "量产");
		} else if (propertyDisplayName.equals("度量单位")) {
			rev = (TCComponentItemRevision) tcComponent;
			rev.getItem().setProperty("uom_tag", value);
		} else {
			super.setValue(tcComponent, index, propertyDisplayName);
		}
	}

	@Override
	public PropertyContainer getPropertyContainer(int index) {
		return PropertyContainer.itemRevision;
	}

	@Override
	public void onSingleStart(int index) {
		System.out.println("第 " + index + " 行开始导入！");
	}

	@Override
	public void onSingleFinish(int index, TCComponent tcc) throws Exception {
		String type = tcc.getType();
		String id = tcc.getProperty("item_id");
		if (type.equals("SF8_RPartRevision")) {
			String value = GetIcsCodeByItemCode.getIcsCode(id);
			cls_manger.saveItemInNode(tcc, value);
		} else if (type.equals("SF8_OPartRevision")) {
			System.out.println("外协不进分类");
		} else if (type.equals("SF8_SPartRevision")) {
			String template = (String) getValue(index, "用户模板类型");
			if (id.startsWith("1220089")) {
				cls_manger.saveItemInNode(tcc, "12204");
			} else {
				if (template.contains("子装配件")) {
					cls_manger.saveItemInNode(tcc, "12201");
				} else if (template.contains("虚拟件")) {
					cls_manger.saveItemInNode(tcc, "12202");
				} else {
					cls_manger.saveItemInNode(tcc, "12203");
				}
			}
		} else if (type.equals("SF8_PPartRevision")) {
			cls_manger.saveItemInNode(tcc, getPPartIcsCode(id));
		} else if (type.equals("SF8_FPartRevision")) {
			String icsCode = id.substring(0, 5);
			cls_manger.saveItemInNode(tcc, icsCode);
		} else {
			cls_manger.saveItemInNode(tcc, "126");
		}
		putInFolder(tcc);
		System.out.println("第 " + index + " 行导入完毕！");
	}

	@Override
	public void onSingleError(int index, Exception e) {
		System.out.println("第 " + index + "出错了：" + e.toString());
	}

	@Override
	public void onStart() throws Exception {
		System.out.println("导入开始");
		if (checkProperties()) {
			System.out.println("必要属性检查通过");
		} else {
			throw new Exception("必要属性检查不通过,物料号或者度量单位");
		}
	}

	static ArrayList<String> ignoreList = new ArrayList<String>();
	static {
		ignoreList.add("序号");
		ignoreList.add("物料号");
		ignoreList.add("名称");
		ignoreList.add("物料类型");
	}

	@Override
	public boolean ignoreProperty(int index, String propertyDisplayName) {
		return ignoreList.contains(propertyDisplayName);
	}

	@Override
	public void onPropertyRealNameNotFound(int index, String propertyName) {
		System.out.println("第：" + index + "行的（" + propertyName + "）属性不存在！");
	}

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) {

	}

	@Override
	public void onSetPropertyError(int index, String propertyDisplayName, Exception e) {

	}

	@Override
	public boolean ignoreRow(int index) {
		return false;
	}

	@Override
	public boolean deleteOldItemWhenItemIdExist(int index) {
		return false;
	}

	/**
	 * 检查物料号及度量单位
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean checkProperties() throws Exception {
		List<Map<String, String>> list = getValues();
		if (list.size() > 0) {
			TCComponentType type = getPropertyContainerType(0);
			Map<String, String> nesPriopertiesMap = list.get(0);
			TCPropertyDescriptor tcpdid = type.getPropertyDescriptor("item_id");
			TCPropertyDescriptor tcpduom = type.getPropertyDescriptor("sf8_unit_of_measure");
			boolean hasItemId = nesPriopertiesMap.containsKey(tcpdid.getDisplayName());
			boolean hasItemUom = nesPriopertiesMap.containsKey(tcpduom.getDisplayName());
			if (hasItemId && hasItemUom) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将新建对象放进文件夹
	 * 
	 * @param tcitem
	 * @return
	 */
	private String putInFolder(TCComponent tcitem) {

		if (folderChildIndex > 800 || folder == null) {
			folder = null;
			try {
				initFolder();
			} catch (Exception e) {
				return e.toString();
			}

		} else {
			try {
				folder.add("contents", tcitem);
				folderChildIndex++;
			} catch (TCException e) {
				return e.toString();
			}
		}
		return "";
	}

	/**
	 * 创建文件夹
	 * 
	 * @throws Exception
	 */
	private void initFolder() throws Exception {

		TCSession session = (TCSession) AIFUtility.getCurrentApplication().getSession();
		TCComponentFolderType ft = (TCComponentFolderType) session.getTypeComponent("Folder");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String time = format.format(new Date());
		String folderName = "物料历史数据-" + time;
		folder = ft.create(folderName, "用于存放导入的历史数据", "Folder");
		folderChildIndex = 0;
		session.getUser().getHomeFolder().add("contents", folder);

	}

	@Override
	public void onSingleMessage(int index, String msg) throws Exception {
		// TODO Auto-generated method stub

	}

	public String getPPartIcsCode(String id) {
		String icsCode = null;
		String tempString = id.substring(3, 6);
		int tempInt = Integer.parseInt(tempString);
		if (tempInt > 0 && tempInt < 301) {
//			核电风机
			icsCode = "12305";
		} else if (tempInt > 300 && tempInt < 421) {
//			"地铁隧道风机"
			icsCode = "12304";
		} else if (tempInt > 420 && tempInt < 601) {
//			"工业离心风机"
			icsCode = "12303";
		} else if ((tempInt > 600 && tempInt < 750) || (tempInt > 800 && tempInt < 977)) {
//			"工民建风机"
			if ((tempInt > 869 && tempInt < 880) || (tempInt > 847 && tempInt < 858)) {
				icsCode = "12302";
			} else {
				icsCode = "12301";
			}
		} else if (tempInt > 749 && tempInt < 800) {
			icsCode = "12307";
		} else if (tempInt == 999) {
			icsCode = "12307";
		}
		return icsCode;
	}

}
