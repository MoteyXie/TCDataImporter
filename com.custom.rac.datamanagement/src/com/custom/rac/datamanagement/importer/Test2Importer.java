package com.custom.rac.datamanagement.importer;

import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.PropertyContainer;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;

public class Test2Importer extends AbstractImporter {

	@Override
	public String getName() {
		return "测试导入2";
	}

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) {
		System.out.println("第 " + index + " 行的【"+propertyDisplayName+" 】导入完成");
	}

	@Override
	public void onSetPropertyError(int index, String propertyDisplayName, Exception e) {
		// TODO Auto-generated method stub

	}
	
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
	public PropertyContainer getPropertyContainer(int index) {
		return PropertyContainer.itemRevision;
	}

	@Override
	public void onSingleStart(int index) throws Exception {
		System.out.println("第 " + index + "行开始了");
		throw new Exception("我有异常");
	}
	
	public String getPropertyRealName(int index, String propertyDisplayName) throws Exception {
		if(index == 3) {
			return "spxxx";
		}else {
			return super.getPropertyRealName(index, propertyDisplayName);
		}
	}

	@Override
	public void onSingleFinish(int index, TCComponent tcc) throws Exception{

	}

	@Override
	public void onSingleError(int index, Exception e) {

	}

	@Override
	public void onStart() {

	}

	@Override
	public void onFinish() {

	}

	@Override
	public boolean ignoreProperty(int index, String propertyDisplayName) {
		if("序号".equals(propertyDisplayName)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean ignoreRow(int index) {
		return false;
	}

	@Override
	public boolean deleteOldItemWhenItemIdExist(int index) {
		return true;
	}

	@Override
	public void onPropertyRealNameNotFound(int index, String propertyName) {
		
	}

}
