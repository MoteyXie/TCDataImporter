package com.custom.rac.itemcode.util;

import java.util.Map;

public class FPartID implements GetItemID {


	@Override
	public String getItemID(Map<String, String> map) {
		String icsCode = map.get("物料分类ID");
		String code1 = map.get("品牌(代码)");
		String code2 = map.get("品牌(代码)");
		String code3 = map.get("极数功率(代码)");
		String prefix = "12"+ icsCode.substring(2, 3)+code1+code2+code3;
		System.out.println(prefix);
		return prefix+"&"+4;
	}

}
