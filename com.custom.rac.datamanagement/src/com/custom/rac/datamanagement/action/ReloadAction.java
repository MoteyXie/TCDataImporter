package com.custom.rac.datamanagement.action;

import org.eclipse.swt.widgets.Widget;

import com.custom.rac.datamanagement.swtxls.ExcelEventParser;
import com.custom.rac.datamanagement.swtxls.MyTable;
import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.AbstractTableAction;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;
import com.teamcenter.rac.util.MessageBox;

public class ReloadAction extends AbstractTableAction {

	public ReloadAction(ExcelTableViewPart tableViewPart) {
		super(tableViewPart);
	}

	@Override
	public void run(Widget widget) throws Exception {
		if (AbstractImporter.exit.equals("暂停")) {
			MessageBox.post("已暂停该程序的运行，请先点击开始运行程序", "提示", MessageBox.INFORMATION);
		} else {
			AbstractImporter.exit = "";
			if (OpenFileAction.lastSelectFile != null && OpenFileAction.lastSelectFile.length() > 0) {
				ExcelEventParser eep = new ExcelEventParser(OpenFileAction.lastSelectFile);
				MyTable myTable = eep.parse();
				tableViewPart.load(myTable);
			}
		}

	}

}
