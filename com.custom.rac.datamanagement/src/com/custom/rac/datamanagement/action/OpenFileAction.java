package com.custom.rac.datamanagement.action;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Widget;

import com.custom.rac.datamanagement.swtxls.ExcelEventParser;
import com.custom.rac.datamanagement.swtxls.MyTable;
import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.AbstractTableAction;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;
import com.teamcenter.rac.util.MessageBox;

public class OpenFileAction extends AbstractTableAction {

	public static String lastSelectFile = null;

	public OpenFileAction(ExcelTableViewPart tableViewPart) {
		super(tableViewPart);
	}

	@Override
	public void run(Widget widget) throws Exception {
		if (AbstractImporter.exit.equals("暂停")) {
			MessageBox.post("已暂停该程序的运行，请先点击开始运行程序", "提示", MessageBox.INFORMATION);
		} else {
			AbstractImporter.exit = "";
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

}
