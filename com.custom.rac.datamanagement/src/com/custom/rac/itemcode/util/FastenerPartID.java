package com.custom.rac.itemcode.util;

import java.util.Map;

public class FastenerPartID implements GetItemID {

	@Override
	public String getItemID(Map<String, String> map) {
		String icsCode = map.get("物料分类ID");
		String prefix = "1210"+icsCode.substring(3);
		return prefix+"&"+6;	
	}

}
