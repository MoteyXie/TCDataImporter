package com.custom.rac.datamanagement.importer;

import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.PropertyContainer;
import com.teamcenter.rac.kernel.TCComponentItemType;

/**
 * 图纸导入工具
 * @author Administrator
 *
 */
public class SFGKDesignImporter extends AbstractImporter {

	@Override
	public String getName() {
		return "图纸导入程序";
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
	public TCComponentItemType getItemType(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertyContainer getPropertyContainer(int index) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onSingleStart(int index) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSingleFinish(int index) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSingleError(int index, Exception e) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinish() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean ignoreProperty(int index, String propertyDisplayName) throws Exception {
		// TODO Auto-generated method stub
		return false;
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
	public void onPropertyRealNameNotFound(int index, String propertyName) throws Exception {
		// TODO Auto-generated method stub

	}

}
