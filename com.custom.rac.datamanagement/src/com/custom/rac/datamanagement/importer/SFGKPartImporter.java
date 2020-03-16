package com.custom.rac.datamanagement.importer;

import java.util.ArrayList;
import java.util.HashMap;

import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.PropertyContainer;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;

public class SFGKPartImporter extends AbstractImporter {
	
	private static HashMap<String, String> typeMap = new HashMap<String, String>();
	static {
		typeMap.put("成品", "SF8_PPart");
		typeMap.put("半成品", "SF8_SPart");
		typeMap.put("毛坯", "SF8_Wpart");
		typeMap.put("原材料", "SF8_RPart");
		typeMap.put("电机", "SF8_FPart");	
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
	public TCComponentItemType getItemType(int index) {
		String type = (String) getValue(index, "物料类型");
		String reltype = typeMap.get(type);
		TCSession session = (TCSession) AIFUtility.getDefaultSession();
		TCComponentItemType itemType = null;
		try {		
			itemType = (TCComponentItemType) session.getTypeComponent(reltype);
		} catch (TCException e) {
			e.printStackTrace();
		}	
		return itemType;
	}
	
	@Override
	public void setValue(TCComponent tcComponent, int index, String propertyDisplayName) throws Exception {
		
		super.setValue(tcComponent, index, propertyDisplayName);
		
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
		System.out.println("第 " + index + " 行导入完毕！");
	}
	

	@Override
	public void onSingleError(int index, Exception e) {
		System.out.println("第 " + index +"出错了：" + e.toString());
	}

	@Override
	public void onStart() {
		System.out.println("导入开始");
	}

	@Override
	public void onFinish() {
		System.out.println("导入结束，请查看导入日志");
	}
	
	static ArrayList<String> ignoreList = new ArrayList<String>();
	static{
		ignoreList.add("序号");
		ignoreList.add("物料编码");
		ignoreList.add("名称");
	}

	@Override
	public boolean ignoreProperty(int index, String propertyDisplayName) {
		return ignoreList.contains(propertyDisplayName);
	}

	@Override
	public void onPropertyRealNameNotFound(int index, String propertyName) {
		
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

}
