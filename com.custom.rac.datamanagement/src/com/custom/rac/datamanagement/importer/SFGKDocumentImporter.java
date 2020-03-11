package com.custom.rac.datamanagement.importer;

import com.custom.rac.datamanagement.driver.IImportDriver;
import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.PropertyContainer;
import com.teamcenter.rac.kernel.TCComponentItemType;

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
	public void loadData(Object data) {
		
	}

	@Override
	public void loadDriver(IImportDriver driver) {
		
	}

	@Override
	public TCComponentItemType getItemType(int index) {
		return null;
	}

	@Override
	public PropertyContainer getPropertyContainer(int index) {
		return null;
	}

	@Override
	public void onSingleStart(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSingleFinish(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSingleError(int index, Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean ignoreProperty(int index, String propertyDisplayName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onPropertyRealNameNotFound(int index, String propertyName) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteOldItemWhenItemIdExist(int index) {
		// TODO Auto-generated method stub
		return false;
	}

}
