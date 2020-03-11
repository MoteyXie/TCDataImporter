package com.custom.rac.datamanagement.bean;

import java.lang.reflect.Constructor;

import com.custom.rac.datamanagement.util.AbstractTableAction;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;

public class ToolBarItemBean {

	private String name;
	private String iconName;
	private String actionName;
	
	public ToolBarItemBean() {
		
	}
	
	public ToolBarItemBean(String name, String iconName, String actionName) {
		super();
		this.name = name;
		this.iconName = iconName;
		this.actionName = actionName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIconName() {
		return iconName;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	public AbstractTableAction getAction(ExcelTableViewPart tableViewPart) throws Exception {
		if(actionName == null || actionName.length() < 1)return null;
		Class<?> cls = Class.forName("com.custom.rac.datamanagement.action."+actionName);
		Constructor<?> ct = cls.getConstructor(ExcelTableViewPart.class);
		return (AbstractTableAction) ct.newInstance(tableViewPart);
	}
	
	
}
