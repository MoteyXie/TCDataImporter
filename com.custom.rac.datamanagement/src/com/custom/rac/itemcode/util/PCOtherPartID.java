package com.custom.rac.itemcode.util;

import java.util.Map;

public class PCOtherPartID implements GetItemID{

	@Override
	public String getItemID(Map<String, String> map) {
		String prefix = "1269999";
		return prefix+"&"+7;	
	}
	
}
