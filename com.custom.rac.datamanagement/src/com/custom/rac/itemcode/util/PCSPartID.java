package com.custom.rac.itemcode.util;

import java.util.HashMap;
import java.util.Map;

public class PCSPartID implements GetItemID {

	private HashMap<String, String> propertyMap = new HashMap<String, String>();
	@Override
	public String getItemID(Map<String, String> map) {
		String code1 = map.get("零部件(代码)");
		String code2 = map.get("型号(代码)");
		String prefix = "126"+code1+code2;
		System.out.println(prefix);
		return prefix+"&"+4;
		
	}
	
	public HashMap<String, String> getPropertyMap() {

		// TODO Auto-generated method stub
		return null;
	}

}