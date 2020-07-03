package com.custom.rac.itemcode.util;

import java.util.Map;

public class AirValvePartID implements GetItemID {

	@Override
	public String getItemID(Map<String, String> map) {
		String prefix = "122008900";
		return prefix+"&"+5;	
	}

}
