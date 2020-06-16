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
		typeMap.put("成品", "SF8_PPart");
		typeMap.put("半成品", "SF8_SPart");
		typeMap.put("毛坯", "SF8_WPart");
		typeMap.put("原材料", "SF8_RPart");
		typeMap.put("电机", "SF8_FPart");	
		typeMap.put("外协件", "SF8_OPart");
		typeMap.put("推式组件", "SF8_BPart");
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
	public TCComponentItemType getItemType(int index) throws Exception{
		String type = (String) getValue(index, "物料类型");
		String reltype = typeMap.get(type);	
		try {		
			itemType = (TCComponentItemType) session.getTypeComponent(reltype);
		} catch (TCException e) {
			e.printStackTrace();
		}	
		if(itemType==null) {
			throw new Exception("物料类型不存在");
		}	
		return itemType;
	}
	
	@Override
	public void setValue(TCComponent tcComponent, int index, String propertyDisplayName) throws Exception {
		String value = getValue(index, propertyDisplayName)+ "";
		if (propertyDisplayName.equals("物料状态")) {
			MyStatusUtil.setStatus(tcComponent, value);
		}else if(propertyDisplayName.equals("物料分类ID")){
			String type = tcComponent.getType();
			String id = tcComponent.getProperty("item_id");
			if(type.equals("SF8_RPartRevision")) {
				value = GetIcsCodeByItemCode.getIcsCode(id);
				cls_manger.saveItemInNode(tcComponent, value);
			}else {
				cls_manger.saveItemInNode(tcComponent, value);	
			}
			
		}else if(propertyDisplayName.equals("度量单位")) {
			rev = (TCComponentItemRevision) tcComponent;
			rev.getItem().setProperty("uom_tag", value);
		}else {
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
	public void onSingleFinish(int index, TCComponent tcc) throws Exception{
		putInFolder(tcc);
		System.out.println("第 " + index + " 行导入完毕！");
	}
	

	@Override
	public void onSingleError(int index, Exception e) {
		System.out.println("第 " + index +"出错了：" + e.toString());
	}

	@Override
	public void onStart() throws Exception{
		System.out.println("导入开始");
		if(checkProperties()) {
			System.out.println("必要属性检查通过");			
		}else {
			throw new Exception("必要属性检查不通过");
		}
	}

	@Override
	public void onFinish() {
		System.out.println("导入结束，请查看导入日志");
	}
	
	static ArrayList<String> ignoreList = new ArrayList<String>();
	static{
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
	

	/**检查物料号及度量单位
	 * @return
	 * @throws Exception
	 */
	private boolean checkProperties() throws Exception {
		List<Map<String, String>> list = getValues();
		if(list.size()>0) {
			TCComponentType type = getPropertyContainerType(0);
			Map<String, String> nesPriopertiesMap = list.get(0);
			TCPropertyDescriptor tcpdid = type.getPropertyDescriptor("item_id");
			TCPropertyDescriptor tcpduom = type.getPropertyDescriptor("sf8_unit_of_measure");			
			boolean hasItemId = nesPriopertiesMap.containsKey(tcpdid.getDisplayName());
			boolean hasItemUom = nesPriopertiesMap.containsKey(tcpduom.getDisplayName());
			if(hasItemId&&hasItemUom) {
				return true;
			}
		}
		return false;	   
	}
	
	
	
	/**将新建对象放进文件夹
	 * @param tcitem
	 * @return
	 */
	private String putInFolder(TCComponent tcitem) {
		
		if(folderChildIndex > 800 || folder == null){
			folder = null;
			try {
				initFolder();
			} catch (Exception e) {
				return e.toString();
			}
			
		}else{
			try {
				folder.add("contents", tcitem);
				folderChildIndex++;
			} catch (TCException e) {
				return e.toString();
			}			
		}	
		return "";
	}
	
	/**创建文件夹
	 * @throws Exception
	 */
	private void initFolder() throws Exception {
		
		TCSession session = (TCSession) AIFUtility.getCurrentApplication().getSession();
		TCComponentFolderType ft = (TCComponentFolderType) session.getTypeComponent("Folder");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String time=format.format(new Date());
		String folderName = "物料历史数据-"+time;
		folder = ft.create(folderName, "用于存放导入的历史数据", "Folder");
		folderChildIndex = 0;
		session.getUser().getHomeFolder().add("contents", folder);
		
	}
	
//	/**
//	 * 临时保存数据
//	 */
//	public void saveExcel() {
//		
//	}

	@Override
	public void onSingleMessage(int index, String msg) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
