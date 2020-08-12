package com.custom.rac.itemcode.util;

import java.util.HashMap;
import java.util.Map;

import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.stylesheet.PropertyLOVDisplayer;

public class SPartID implements GetItemID {

	private HashMap<String, String> propertyMap = new HashMap<String, String>();
	@Override
	public String getItemID(Map<String, String> map) {
		String icsCode = map.get("物料分类ID");
		String code1 = map.get("零部件(代码)");
		String code2 = map.get("型号(代码)");
		String prefix = "12"+ icsCode.substring(2, 3)+code1+code2;
		System.out.println(prefix);
		return prefix+"&"+4;
		
	}
	
	
	public HashMap<String, String> getPropertyMap() {
		
		// TODO Auto-generated method stub
		return null;
	}
	

	

}
