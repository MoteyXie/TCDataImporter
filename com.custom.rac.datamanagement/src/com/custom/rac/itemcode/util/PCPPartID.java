package com.custom.rac.itemcode.util;

import java.util.HashMap;
import java.util.Map;

import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCSession;

public class PCPPartID implements GetItemID {

	private HashMap<String, String> propertyMap = new HashMap<String, String>();
	@Override
	public String getItemID(Map<String, String> map) throws Exception {
		String prefix = null;
		String code1 = map.get("型号(代码)");				
		String code2 =map.get("机号(代码)");
		String code3 = map.get("传动方式(代码)");
		String drivename = searchFormComp(code3,"传动方式");
		propertyMap.put("sf8_kind_of_drive", drivename);
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
	
	/**查找表单
	 * @param desc
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public String searchFormComp(String desc,String type) throws Exception {
		TCSession session = (TCSession) AIFUtility.getDefaultSession();
		TCComponent[] result = session.search("SF8_SearchLOVForm", new String[] { "描述","类型" }, new String[] { desc,type });
		return result[0].getProperty("object_name");
	}
	
	public HashMap<String, String> getPropertyMap() {
		// TODO Auto-generated method stub
		return propertyMap;
	}
}
