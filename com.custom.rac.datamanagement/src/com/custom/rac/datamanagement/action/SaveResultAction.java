package com.custom.rac.datamanagement.action;

import org.eclipse.swt.widgets.Widget;

import com.custom.rac.datamanagement.util.AbstractTableAction;
import com.custom.rac.datamanagement.util.WriteDataToExcel;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;
import com.teamcenter.rac.util.MessageBox;

public class SaveResultAction extends AbstractTableAction {

	public SaveResultAction(ExcelTableViewPart tableViewPart) {
		super(tableViewPart);
	}

	@Override
	public void run(Widget widget) throws Exception {
		// 先判断程序是否在进行中
		tableViewPart.setExecuting(false);
		boolean isExecuting = tableViewPart.isExecuting();
		if (isExecuting) {
			throw new Exception("程序执行中，无法保存数据！");
		}
		// 文件打开路径
		String lastSelectedFilePath = OpenFileAction.lastSelectFile;
		if (lastSelectedFilePath == null) {
			throw new Exception("没有数据保存！");
		}
		WriteDataToExcel.WriteData(tableViewPart, lastSelectedFilePath, lastSelectedFilePath);
		MessageBox.post("保存成功，保存路径：" + lastSelectedFilePath, "提示", MessageBox.INFORMATION);

	}

}
