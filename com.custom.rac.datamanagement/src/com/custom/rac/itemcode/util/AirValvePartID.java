package com.custom.rac.itemcode.util;

import java.util.Map;

public class AirValvePartID implements GetItemID {

	@Override
	public String getItemID(Map<String, String> map) {
		String prefix = "1220089";
		return prefix+"&"+7;	
	}

}
