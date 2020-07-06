package com.custom.rac.itemcode.util;

import java.util.Map;

public class SPartID implements GetItemID {


	@Override
	public String getItemID(Map<String, String> map) {
		String icsCode = map.get("物料分类ID");
		String code1 = map.get("零部件(代码)");
		String code2 = map.get("型号(代码)");
		String code3 = map.get("机号(代码)");
//		if(code2.equals("000")&&code3.equals("00")) {
//			String prefix = "12"+ icsCode.substring(2, 3)+code1+code2;
//			System.out.println(prefix);
//			return prefix+"&"+4;
//		}else if(!code2.equals("000")&&code3.equals("00")){
//			String prefix = "12"+ icsCode.substring(2, 3)+code1+code2;
//			System.out.println(prefix);
//			return prefix+"&"+4;
//		}else {
			String prefix = "12"+ icsCode.substring(2, 3)+code1+code2+code3;
			System.out.println(prefix);
			return prefix+"&"+2;
//		}
	}

}
