package com.custom.rac.itemcode.util;

import java.util.Map;

public class SPartID implements GetItemID {


	@Override
	public String getItemID(Map<String, String> map) {
		String icsCode = map.get("物料分类ID");
		String code1 = map.get("零部件(代码)");
		String code2 = map.get("型号(代码)");
		String prefix = "12"+ icsCode.substring(2, 3)+code1+code2;
		System.out.println(prefix);
		return prefix+"&"+4;
		
	}

}
