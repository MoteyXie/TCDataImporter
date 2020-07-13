package com.custom.rac.datamanagement.util;

import com.custom.rac.datamanagement.views.ExcelTableViewPart;

public abstract class AbstractTableAction implements IAction {

	protected static ExcelTableViewPart tableViewPart;

	private String params;

	public AbstractTableAction(ExcelTableViewPart tableViewPart) {
		AbstractTableAction.tableViewPart = tableViewPart;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getParams() {
		return params;
	}

	public String[] getParamArray() {
		if (params == null)
			return null;
		return params.split(",");
	}

}
