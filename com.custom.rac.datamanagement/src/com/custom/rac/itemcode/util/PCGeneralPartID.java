package com.custom.rac.itemcode.util;

import java.util.Map;

public class PCGeneralPartID implements GetItemID{

	@Override
	public String getItemID(Map<String, String> map) {
		String code1 = map.get("零部件(代码)");
		String code2 = map.get("型号(代码)");
		String code3 = map.get("机号(代码)");
		String prefix = "126"+code1+code2+code3;
		System.out.println(prefix);
		return prefix+"&"+7;

	}
	
}
