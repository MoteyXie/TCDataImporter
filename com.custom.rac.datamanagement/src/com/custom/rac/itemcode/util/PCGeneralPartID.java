package com.custom.rac.itemcode.util;

import java.util.HashMap;
import java.util.Map;

public class PCGeneralPartID implements GetItemID{

	private HashMap<String, String> propertyMap = new HashMap<String, String>();
	@Override
	public String getItemID(Map<String, String> map) {
		String code1 = map.get("零部件(代码)");
		String prefix = "126"+code1;
		System.out.println(prefix);
		return prefix+"&"+7;

	}
	
	@Override
	public HashMap<String, String> getPropertyMap() {
	
		return null;
	}
	
}
