package com.custom.rac.datamanagement.util;

import java.util.HashMap;

import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.services.rac.workflow.WorkflowService;
import com.teamcenter.services.rac.workflow._2007_06.Workflow;
import com.teamcenter.soa.client.model.ErrorStack;
import com.teamcenter.soa.client.model.ServiceData;

//Require-Bundle: TcSoaWorkflowRac
public class MyStatusUtil {
	
	public static final String OP_APPEND = "Append";
	public static final String OP_DELETE = "Delete";
	public static HashMap<String, String> map = null;	
	static {
		map = new HashMap<>();		
	    map.put("发布", "TCM Released");
	    map.put("失效", "SF8_Invalid");
	    map.put("限用", "SF8_Restrict");
	    map.put("量产", "SF8_Valid");		
	}
	
	
	public static String setStatus(TCComponent obj, String statusName) {
		
		statusName = map.get(statusName);
		String ret = null;		
		try {
			if (obj == null) {
				return "Target object should not be NULL";
			}
			
			if (statusName == null) {
				return "Status Name should not be NULL or empty!";
			}
			
			statusName = statusName.trim();
			if (statusName.length() == 0) {
				return "Status Name should not be NULL or empty!";
			}
			
			TCSession ss = (TCSession) AIFUtility.getDefaultSession();
			WorkflowService ws = WorkflowService.getService(ss);
			
			Workflow.ReleaseStatusInput[] input = new Workflow.ReleaseStatusInput[2];
			input[0] = new Workflow.ReleaseStatusInput();
			input[0].objects = new TCComponent[] { obj };
			input[0].operations = new Workflow.ReleaseStatusOption[1];
			input[0].operations[0] = new Workflow.ReleaseStatusOption();
			input[0].operations[0].operation = OP_DELETE;
			
			input[1] = new Workflow.ReleaseStatusInput();
			input[1].objects = new TCComponent[] { obj };
			input[1].operations = new Workflow.ReleaseStatusOption[1];
			input[1].operations[0] = new Workflow.ReleaseStatusOption();
			input[1].operations[0].newReleaseStatusTypeName = statusName;
			input[1].operations[0].operation = OP_APPEND;

			Workflow.SetReleaseStatusResponse resp = ws.setReleaseStatus(input);
			
			ret = handleServiceData(resp.serviceData);
			
		} catch(Exception e) {
			ret = "MyStatusUtil.setStatus(TCComponent, String) Error: " + e.getMessage();
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public static String appendStatus(TCComponent obj, String statusName) {
		String ret = null;
		
		try {
			if (obj == null) {
				return "Target object should not be NULL";
			}
			
			if (statusName == null) {
				return "Status Name should not be NULL or empty!";
			}
			
			statusName = statusName.trim();
			if (statusName.length() == 0) {
				return "Status Name should not be NULL or empty!";
			}
			
			TCSession ss = (TCSession) AIFUtility.getDefaultSession();
			WorkflowService ws = WorkflowService.getService(ss);
			
			Workflow.ReleaseStatusInput[] input = new Workflow.ReleaseStatusInput[1];
			input[0] = new Workflow.ReleaseStatusInput();
			input[0].objects = new TCComponent[] { obj };
			input[0].operations = new Workflow.ReleaseStatusOption[1];
			input[0].operations[0] = new Workflow.ReleaseStatusOption();
			input[0].operations[0].newReleaseStatusTypeName = statusName;
			input[0].operations[0].operation = OP_APPEND;

			Workflow.SetReleaseStatusResponse resp = ws.setReleaseStatus(input);
			
			ret = handleServiceData(resp.serviceData);
			
		} catch(Exception e) {
			ret = "MyStatusUtil.appendStatus(TCComponent, String) Error: " + e.getMessage();
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public static String clearStatus(TCComponent obj) {
		String ret = null;
		
		try {
			if (obj == null) {
				return "Target object should not be NULL";
			}
			
			TCSession ss = (TCSession) AIFUtility.getDefaultSession();
			WorkflowService ws = WorkflowService.getService(ss);
			
			Workflow.ReleaseStatusInput[] input = new Workflow.ReleaseStatusInput[1];
			input[0] = new Workflow.ReleaseStatusInput();
			input[0].objects = new TCComponent[] { obj };
			input[0].operations = new Workflow.ReleaseStatusOption[1];
			input[0].operations[0] = new Workflow.ReleaseStatusOption();
			input[0].operations[0].operation = OP_DELETE;

			Workflow.SetReleaseStatusResponse resp = ws.setReleaseStatus(input);
			
			ret = handleServiceData(resp.serviceData);
			
		} catch(Exception e) {
			ret = "MyStatusUtil.clearStatus(TCComponent) Error: " + e.getMessage();
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public static String getCurrentStatusName(TCComponent obj) {
		String ret = null;
		
		try {
			if (obj == null) {
				return "Target object should not be NULL";
			}
			
			ret = obj.getProperty("last_release_status");
			
		} catch(Exception e) {
			ret = "$ERROR$";
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public static String handleServiceData(final ServiceData sData) {
		int noPartErrors = sData.sizeOfPartialErrors();
		
		if (noPartErrors > 0) {
			String errorMessage = "";
			for (int i = 0; i < noPartErrors; i++) {
				ErrorStack errorStack = sData.getPartialError(i);
				String[] messages = errorStack.getMessages();
				for (int j = 0; messages != null && j < messages.length; j++) {
					errorMessage = errorMessage + messages[j] + "\n";
				}
			}
			
			if (errorMessage.length() > 0) {
				return errorMessage;
			}
		}
		
		return null;
	}
}
