package com.custom.rac.datamanagement.importer;

import java.io.File;

import com.custom.rac.datamanagement.util.XMLResult;
import com.sfgk.sie.webservice.SFGKServiceProxy;
import com.teamcenter.rac.kernel.TCComponent;

/**
 * 图纸导入工具
 * @author Administrator
 *
 */
public class SFGKGDDesignImporter extends SFGKDesignImporter {

	TCComponent com;
	
	@Override
	public String getName() {
		return "上风高科结构图纸导入程序";
	}
	
	public TCComponent newTCComponent(int index) throws Exception {
		TCComponent rev = super.newTCComponent(index);
		if (isGDDesign(index)) {
			com = rev;
		}
		return rev;
	}
	
	@Override
	public void onSingleStart(int index) throws Exception {
		StringBuilder sb = new StringBuilder();
		Object obj = getValue(index, "电子档存放地址");
		if (obj == null || obj.toString().trim().isEmpty()) {
			sb.append("电子档存放地址不能为空/");
		} else {
			String value = obj.toString().trim();
			file = new File(value);
			if (file == null || !file.exists() || !file.isFile()) {
				sb.append("电子档存放地址路径找不到文件/");				
			}
		}
		String msg = sb.toString();
		if (msg != null && !msg.isEmpty()) {
			throw new Exception(msg);
		}
	}
	
	boolean isGDDesign(int index) {
		String type = getValue(index, "图纸类型") + "";
		if (type.equals("核电产品图")) {
			return true;
		}
		return false;
	}
	
//	@Override
//	public String getItemId(int index) throws Exception {
//		String value = "";
//		if (isGDDesign(index)) {
//			value = getValueFromRealName(index, "item_id");
//			if (value == null || value.length() == 0) {
//				throw new Exception("产品总装图不能为空！");
//			}			
//		} else {
//			if (com == null) {
//				throw new Exception("产品总装图不能为空！");
//			}
//			value = com.getProperty("item_id");
//		}
//		value += "-";
//		String id = getID(value , 3);
//		driver.onNewItemId(index, id);
//		return id;
//	}
	
	@Override
	public String newItemId(int index) throws Exception {
		String value = "";
		if (isGDDesign(index)) {
			value = getValue(index, "产品型号") + "";
			if (value == null || value.length() == 0) {
				throw new Exception("产品型号不能为空！");
			}			
		} else {
			if (com == null) {
				throw new Exception("轨道产品图不能为空！");
			}
			value = com.getProperty("item_id");
		}
		value += "-";
		return getID(value , 2);
	}
	
	public String getID(String prefix, int serialLength) throws Exception{
		SFGKServiceProxy proxy = new SFGKServiceProxy();
		String xml = proxy.getID(prefix, serialLength);
		XMLResult result = XMLResult.read(xml);
		String error = result.error;
		if (error != null && !error.isEmpty()) {
			throw new Exception(error);
		}
        return result.value;
	}


}
