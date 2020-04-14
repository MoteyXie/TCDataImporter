package com.custom.rac.datamanagement.util;

import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCPreferenceService;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.kernel.TCPropertyDescriptor;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.kernel.TCTextService;
import com.teamcenter.rac.util.Registry;

public class MyTextUtil {
	public static String translate(String text) {
		String ret = text;
		
		TCSession ss = (TCSession) AIFUtility.getDefaultSession();
		TCTextService ts = ss.getTextService();
		try {
			String t2 = ts.getTextValue(text);
			return t2;
		} catch (TCException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public static String translate(String text, @SuppressWarnings("rawtypes") Class clz) {
		try {
			String ret = Registry.getRegistry(clz).getString(text);
			return ret;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getDisplayValue(TCProperty p) {
		if (p == null) return null;
		
		return p.toString();
	}
	
	public static String getRealValue(TCProperty p) {
		if (p == null) return null;
		
		if (p.getPropertyValue() == null) {
			return null;
		}
		
		return p.getPropertyValue().toString();
	}
	
	public static String getDisplayName(TCProperty p) {
		if (p == null) return null;
		return getDisplayName(p.getDescriptor());
	}
	
	public static String getDisplayName(TCPropertyDescriptor pd) {
		if (pd == null) return null;
		
		TCSession ss = (TCSession) AIFUtility.getDefaultSession();
		TCPreferenceService service = ss.getPreferenceService();
		Boolean old = service.getLogicalValue("TC_display_real_prop_names");
		try {
			service.setLogicalValue("TC_display_real_prop_names", false);
			String displayName = pd.getDisplayName();
			service.setLogicalValue("TC_display_real_prop_names", old);
			return displayName;
		} catch (TCException e) {
			e.printStackTrace();
		}
		
		return pd.getDisplayName();
	}
	
	public static boolean setDisplayRealPropertyName(boolean onOff) {
		try {
			TCSession ss = (TCSession) AIFUtility.getDefaultSession();
			TCPreferenceService service = ss.getPreferenceService();
			service.setLogicalValue("TC_display_real_prop_names", onOff);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
