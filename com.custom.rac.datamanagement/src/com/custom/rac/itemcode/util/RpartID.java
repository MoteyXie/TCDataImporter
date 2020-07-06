package com.custom.rac.itemcode.util;

import java.util.Map;

public class RpartID implements GetItemID {

	@Override
	public String getItemID(Map<String, String> map){

		String icsCode = map.get("物料分类ID");
		String prefixCode = icsCode.substring(3)+"000";
		String prefix = "1210"+prefixCode;		
		return prefix+"&"+5;
	}

}
