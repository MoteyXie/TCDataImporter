package com.custom.rac.itemcode.util;

import java.util.Map;

public class GetItemIDFactory {
	
	
	public static String getID(Map<String, String> map) {
		String id = null;
		String icsCode = map.get("物料分类ID");
		String prefixCode = icsCode.substring(0, 3);
		switch (prefixCode) {
		case "121":
					if(icsCode.startsWith("12103")||icsCode.startsWith("12105")||icsCode.startsWith("12108")||icsCode.startsWith("12110")) {
						id = new FastenerPartID().getItemID(map);
					}else if(icsCode.startsWith("12118")||icsCode.startsWith("12122")) {
						id = new DamperPartID().getItemID(map);
					}else {
						id = new RpartID().getItemID(map);
					}
					break;
		case "122": 
					if(icsCode.startsWith("12204")) {
						id = new AirValvePartID().getItemID(map);
					}else {
						id = new SPartID().getItemID(map);
					}
					break;
		case "123":	
					if(icsCode.startsWith("12307")) {
						id = new EnvirPartID().getItemID(map);
					}else{
						id = new PPartID().getItemID(map);
					}		
					break;
		case "124": id = new SPartID().getItemID(map);
			break;
		case "125": id = new FPartID().getItemID(map);			
			break;
		case "126":
			if(icsCode.startsWith("12601")) {
				//风阀类
				id  = new PCAirValvePartID().getItemID(map);
			}else if(icsCode.startsWith("12602")){
				//风机成品类
				id = new PCPPartID().getItemID(map);
			}else if(icsCode.startsWith("12603")) {
				//零部件类
				id = new PCSPartID().getItemID(map);
			}else if(icsCode.startsWith("12604")) {
				//通用类
				id = new PCGeneralPartID().getItemID(map);
			}else{
				//其它类
				id = new PCOtherPartID().getItemID(map);
			}
			break;
		default:
			break;
		}		
		return id;
	}
}
