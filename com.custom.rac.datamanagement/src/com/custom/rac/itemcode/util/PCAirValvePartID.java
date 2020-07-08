package com.custom.rac.itemcode.util;

import java.util.Map;

public class PCAirValvePartID implements GetItemID {

	@Override
	public String getItemID(Map<String, String> map) {
		String prefix = "1260089";
		return prefix+"&"+7;	
	}
}