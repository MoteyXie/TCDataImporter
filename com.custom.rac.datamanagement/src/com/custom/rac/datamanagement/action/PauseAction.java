package com.custom.rac.datamanagement.action;

import org.eclipse.swt.widgets.Widget;

import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.AbstractTableAction;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;
import com.teamcenter.rac.util.MessageBox;

public class PauseAction extends AbstractTableAction {

	public PauseAction(ExcelTableViewPart tableViewPart) {
		super(tableViewPart);
	}

	@Override
	public void run(Widget widget) throws Exception {
		Thread thread = ImportAction.getThread();
		if (AbstractImporter.exit.equals("停止")) {
			MessageBox.post("该程序已停止运行，请重新加载开始", "提示", MessageBox.INFORMATION);
		} else if (thread == null) {
			MessageBox.post("程序还没开始运行！", "提示", MessageBox.INFORMATION);
		} else {
			AbstractImporter.exit = "暂停";
			MessageBox.post("暂停程序运行！", "提示", MessageBox.INFORMATION);
		}
	}

}
