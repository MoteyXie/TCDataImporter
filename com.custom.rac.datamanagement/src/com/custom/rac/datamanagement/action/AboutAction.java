package com.custom.rac.datamanagement.action;

import org.eclipse.swt.widgets.Widget;

import com.custom.rac.datamanagement.util.AboutDialog;
import com.custom.rac.datamanagement.util.AbstractTableAction;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;

public class AboutAction extends AbstractTableAction {

	public AboutAction(ExcelTableViewPart tableViewPart) {
		super(tableViewPart);
	}

	@Override
	public void run(Widget widget) throws Exception {
		AboutDialog aboutDialog = AboutDialog.getInstance();
		aboutDialog.loadDialog();
		aboutDialog.setVisible(true);
	}

}
