package com.custom.rac.datamanagement.importer;

import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.PropertyContainer;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCSession;

public class SFGKDocumentImporter extends AbstractImporter {

	@Override
	public String getName() {
		return "上风高科文档导入程序";
	}

	@Override
	public void execute() {
		System.out.println("执行 ： " + getName());
	}

	@Override
	public TCComponentItemType getItemType(int index) throws Exception{
		TCSession session = (TCSession) AIFUtility.getDefaultSession();
		TCComponentItemType type = (TCComponentItemType) session.getTypeComponent("SF8_Document");
		return type;
	}

	@Override
	public PropertyContainer getPropertyContainer(int index) {
		return PropertyContainer.itemRevision;
	}

	@Override
	public void onSingleStart(int index) {
		
	}

	@Override
	public void onSingleFinish(int index, TCComponent tcc) throws Exception{
		System.out.println("在结束后，要导入数据集");
	}

	@Override
	public void onSingleError(int index, Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart() {
		
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean ignoreProperty(int index, String propertyDisplayName) {
		if (propertyDisplayName.equals("文档编号") || propertyDisplayName.equals("版本") 
			|| propertyDisplayName.equals("描述") || propertyDisplayName.equals("文档名称")
			|| propertyDisplayName.equals("文档分类ID") || propertyDisplayName.equals("电子档存放地址")){
			return true;
		}
		return false;
	}

	@Override
	public void onPropertyRealNameNotFound(int index, String propertyName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) {
		System.out.println("第" + index + "行");
		
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
		return false;
	}

}
