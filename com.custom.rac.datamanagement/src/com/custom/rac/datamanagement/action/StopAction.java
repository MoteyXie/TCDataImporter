package com.custom.rac.datamanagement.action;

import org.eclipse.swt.widgets.Widget;

import com.custom.rac.datamanagement.util.AbstractTableAction;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;
import com.teamcenter.rac.util.MessageBox;

public class StopAction extends AbstractTableAction {

	public StopAction(ExcelTableViewPart tableViewPart) {
		super(tableViewPart);
	}

	@Override
	public void run(Widget widget) throws Exception {
		if (ImportAction2.getThread() == null) {
			MessageBox.post("程序还没开始运行！", "提示", MessageBox.INFORMATION);
		} else if (ImportAction2.getThread().getRunState().equals("暂停")) {
			MessageBox.post("已暂停该程序的运行，请先点击开始运行程序", "提示", MessageBox.INFORMATION);
		} else {
			ImportAction2.getThread().setRunState("停止");
		}
	}

}
