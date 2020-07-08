package com.custom.rac.itemcode.util;

import java.util.Map;

public class PCPPartID implements GetItemID {

	@Override
	public String getItemID(Map<String, String> map) {
		String prefix = null;
		String code1 = map.get("型号(代码)");				
		String code2 =map.get("机号(代码)");
		String code3 = map.get("传动方式(代码)");
		if(code2.equals("00")&&code3.equals("0")) {
			prefix = "126"+code1+code2;
			System.out.println(prefix);
			return prefix+"&"+6;
		}else if(!code2.equals("00")&&code3.equals("0")){
			prefix = "126"+code1+code2;
			System.out.println(prefix);
			return prefix+"&"+6;	
		}else {
			prefix = "126"+code1+code2+code3;		
			System.out.println(prefix);
			return prefix+"&5";	
		}
	}
}
