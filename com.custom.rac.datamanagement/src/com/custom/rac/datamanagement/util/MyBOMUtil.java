package com.custom.rac.datamanagement.util;

import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentBOMViewRevisionType;
import com.teamcenter.rac.kernel.TCComponentBOMViewType;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentViewType;
import com.teamcenter.rac.kernel.TCComponentViewTypeType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.kernel.TCSession;

public class MyBOMUtil {
	public static final String BOM_VIEW_TYPE = "PSViewType";
	
	public static TCComponentViewType getViewType(String typeName) {
		if (typeName == null || "".equals(typeName.trim())) return null;
		
		TCComponent[] vts = getAllViewTypes();
		if (vts == null) return null;
		for (TCComponent vt : vts) {
			if (vt.toString().equals(typeName)) {
				return (TCComponentViewType)vt;
			}
			else {
				try {
					TCProperty p = vt.getTCProperty("name");
					String realValue = MyTextUtil.getRealValue(p);
					if (typeName.equals(realValue)) {
						return (TCComponentViewType)vt;
					}
				} catch (TCException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	public static TCComponent[] getAllViewTypes() {
		try {
			TCSession ss = (TCSession) AIFUtility.getDefaultSession();
			TCComponentViewTypeType vtt = (TCComponentViewTypeType)ss.getTypeComponent(BOM_VIEW_TYPE);
			
			TCComponent[] vts = vtt.extent();
			
			return vts;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static TCComponentViewTypeType getTCComponentViewTypeType() {
		try {
			TCSession ss = (TCSession) AIFUtility.getDefaultSession();
			TCComponentViewTypeType vtt = (TCComponentViewTypeType)ss.getTypeComponent(BOM_VIEW_TYPE);
			
			return vtt;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static TCComponentViewType[] getAvailableViewTypes(String itemId, String revId) {
		TCComponentBOMViewRevisionType bvrt = getTCComponentBOMViewRevisionType();
		if (bvrt == null) return null;
		
		try {
			return bvrt.getAvailableViewTypes(itemId, revId);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static TCComponentViewType[] getAvailableViewTypes(TCComponentItemRevision rev) {
		if (rev == null) {
			return null;
		}
		
		try {
			String itemId = rev.getItem().getProperty("item_id");
			String revId = rev.getProperty("current_revision_id");
			
			return getAvailableViewTypes(itemId, revId);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
//	public static TCComponentViewType[] getAvailableViewTypes2(TCComponentItemRevision rev) {
//		if (rev == null) {
//			return null;
//		}
//		
//		TCSession ss = (TCSession) AIFUtility.getDefaultSession();
//		TCComponentViewType types[] = null;
//		StructureService service = StructureService.getService(ss);
//		com.teamcenter.services.internal.rac.structuremanagement._2011_06.Structure.GetAvailableViewTypesResponse resp = null;
//		com.teamcenter.services.internal.rac.structuremanagement._2011_06.Structure.GetAllAvailableViewTypesInput inputs[] = new com.teamcenter.services.internal.rac.structuremanagement._2011_06.Structure.GetAllAvailableViewTypesInput[1];
//		inputs[0] = new com.teamcenter.services.internal.rac.structuremanagement._2011_06.Structure.GetAllAvailableViewTypesInput();
//		inputs[0].clientId = (new Integer(0)).toString();
//		inputs[0].itemRevisionObj = rev;
//		resp = service.getAvailableViewTypes(inputs);
//		
//		try {
//			SoaUtil.checkPartialErrors(resp.serviceData);
//		} catch (TCException e) {
//			e.printStackTrace();
//			return null;
//		}
//		
//		int i = 0;
//		if (resp.viewTypesOutputs[0].viewTags != null) {
//			i = resp.viewTypesOutputs[0].viewTags.length;
//			types = new TCComponentViewType[i];
//		}
//		
//		for (int j = 0; j < i; j++) {
//			types[j] = (TCComponentViewType) resp.viewTypesOutputs[0].viewTags[j];
//		}
//
//		return types;
//	}
	
	public static TCComponentBOMViewType getTCComponentBOMViewType() {
		try {
			TCSession ss = (TCSession) AIFUtility.getDefaultSession();
			TCComponentBOMViewType bvt = (TCComponentBOMViewType)ss.getTypeComponent("BOMView");
			
			return bvt;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static TCComponentBOMViewRevisionType getTCComponentBOMViewRevisionType() {
		try {
			TCSession ss = (TCSession) AIFUtility.getDefaultSession();
			TCComponentBOMViewRevisionType bvrt = (TCComponentBOMViewRevisionType)ss.getTypeComponent("BOMView Revision");
			
			return bvrt;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
