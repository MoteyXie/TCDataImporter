package com.custom.rac.itemcode.util;

import java.util.HashMap;
import java.util.Map;


public interface GetItemID {

	String getItemID(Map<String, String> map) throws Exception;
	
	HashMap<String, String> getPropertyMap();
	
}
