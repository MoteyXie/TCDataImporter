package com.custom.rac.datamanagement.action;

import org.eclipse.swt.widgets.Widget;

import com.custom.rac.datamanagement.driver.ExcelTableViewPartImportDriver;
import com.custom.rac.datamanagement.util.AbstractTableAction;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;

public class TestAction extends AbstractTableAction {

	public TestAction(ExcelTableViewPart tableViewPart) {
		super(tableViewPart);
	}

	@Override
	public void run(Widget widget) throws Exception {
		
		ExcelTableViewPartImportDriver driver = new ExcelTableViewPartImportDriver(tableViewPart, 1);
		driver.onSingleStart(1);
	}

}
