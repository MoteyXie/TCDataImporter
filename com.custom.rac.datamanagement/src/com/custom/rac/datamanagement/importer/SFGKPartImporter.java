package com.custom.rac.datamanagement.importer;

import com.custom.rac.datamanagement.driver.IImportDriver;
import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.PropertyContainer;
import com.teamcenter.rac.kernel.TCComponentItemType;

public class SFGKPartImporter extends AbstractImporter {

	@Override
	public String getName() {
		return "上风高科物料导入程序";
	}

	@Override
	public void execute() {
		System.out.println("执行 ： " + getName());
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
		
	}

	@Override
	public void onSingleFinish(int index) {
		System.out.println("加入分类");
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
		return false;
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
