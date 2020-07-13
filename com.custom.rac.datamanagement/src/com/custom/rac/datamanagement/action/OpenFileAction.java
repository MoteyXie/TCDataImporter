package com.custom.rac.datamanagement.action;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Widget;

import com.custom.rac.datamanagement.swtxls.ExcelEventParser;
import com.custom.rac.datamanagement.swtxls.MyTable;
import com.custom.rac.datamanagement.util.AbstractTableAction;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;

public class OpenFileAction extends AbstractTableAction {

	public static String lastSelectFile = null;

	public OpenFileAction(ExcelTableViewPart tableViewPart) {
		super(tableViewPart);
	}

	@Override
	public void run(Widget widget) throws Exception {
		ImportAction2.setThread(null);
		FileDialog fd = new FileDialog(tableViewPart.getContainer().getShell(), SWT.OPEN);
		fd.setFilterPath(System.getProperty("JAVA.HOME"));
		fd.setFilterExtensions(new String[] { "*.xlsx" });
		fd.setFilterNames(new String[] { "Excel Files(*.xlsx)" });
		String file = fd.open();
		lastSelectFile = file;
		ExcelEventParser eep = new ExcelEventParser(file);

		MyTable myTable = eep.parse();

		tableViewPart.load(myTable);

	}

}
