package com.custom.rac.datamanagement.action;

import org.eclipse.swt.widgets.Widget;

import com.custom.rac.datamanagement.swtxls.ExcelEventParser;
import com.custom.rac.datamanagement.swtxls.MyTable;
import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.AbstractTableAction;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;

public class ReloadAction extends AbstractTableAction {

	public ReloadAction(ExcelTableViewPart tableViewPart) {
		super(tableViewPart);
	}

	@Override
	public void run(Widget widget) throws Exception {
		ImportAction2.setThread(null);
		AbstractImporter.exit = "";
		if (OpenFileAction.lastSelectFile != null && OpenFileAction.lastSelectFile.length() > 0) {
			ExcelEventParser eep = new ExcelEventParser(OpenFileAction.lastSelectFile);
			MyTable myTable = eep.parse();
			tableViewPart.load(myTable);
		}

	}

}
