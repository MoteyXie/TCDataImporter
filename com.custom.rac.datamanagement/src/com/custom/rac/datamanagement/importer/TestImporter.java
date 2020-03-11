package com.custom.rac.datamanagement.importer;

import java.util.ArrayList;

import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.PropertyContainer;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;

public class TestImporter extends AbstractImporter {

	TCComponentItemType itemType;
	@Override
	public TCComponentItemType getItemType(int index) {
		
		if(itemType == null) {
			TCSession session = (TCSession) AIFUtility.getDefaultSession();
			try {
				itemType = (TCComponentItemType) session.getTypeComponent("SF8_PPart");
			} catch (TCException e) {
				e.printStackTrace();
			}
		}
		return itemType;
	}

	@Override
	public void onSingleStart(int index) {
		System.out.println("第 " + index + " 行开始导入！");
	}

	@Override
	public void onSingleFinish(int index) {
		System.out.println("第 " + index + " 行导入完毕！");
	}

	@Override
	public void onStart() {
		System.out.println("开始导入");
	}

	@Override
	public void onFinish() {
		System.out.println("完成导入");
	}

	@Override
	public String getName() {
		return "测试的导入工具";
	}

	@Override
	public void onSingleError(int index, Exception e) {
		System.out.println("第 " + index +"出错了：" + e.toString());
	}
	
	static ArrayList<String> ignoreList = new ArrayList<String>();
	static{
		ignoreList.add("序号");
	}

	@Override
	public boolean ignoreProperty(int index, String propertyDisplayName) {
		return ignoreList.contains(propertyDisplayName);
	}

	@Override
	public void onPropertyRealNameNotFound(int index, String propertyName) {
		
	}

	@Override
	public PropertyContainer getPropertyContainer(int index) {
		return PropertyContainer.itemRevision;
	}

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSetPropertyError(int index, String propertyDisplayName, Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean ignoreRow(int index) {
		return false;
	}

	@Override
	public boolean deleteOldItemWhenItemIdExist(int index) {
		return true;
	}

}
