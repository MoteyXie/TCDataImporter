package com.custom.rac.itemcode.util;

import java.util.Map;

public class EnvirPartID implements GetItemID {

	@Override
	public String getItemID(Map<String, String> map) {
		String code1 = map.get("型号(代码)");
		String prefix = "123"+code1;		
		return prefix+"&"+8;	
	}

}
