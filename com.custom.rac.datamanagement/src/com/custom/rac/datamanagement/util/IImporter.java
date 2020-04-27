package com.custom.rac.datamanagement.util;

import com.custom.rac.datamanagement.driver.IImportDriver;
import com.teamcenter.rac.kernel.TCComponent;

public interface IImporter {
	
	String getName();
	
	void execute()throws Exception;
	
	void loadData(Object data)throws Exception;
	
	void loadDriver(IImportDriver driver)throws Exception;
	
	void onSingleStart(int index)throws Exception;
	
	void onSingleFinish(int index, TCComponent tcc)throws Exception;
	
	void onSingleError(int index, Exception e)throws Exception;
	
	void onSetPropertyFinish(int index, String propertyDisplayName)throws Exception;
	
	void onSetPropertyError(int index, String propertyDisplayName, Exception e)throws Exception;
	
	void onStart() throws Exception;
	
	void onFinish() throws Exception;
	
	void onSingleMessage(int index,String msg) throws Exception;
}
