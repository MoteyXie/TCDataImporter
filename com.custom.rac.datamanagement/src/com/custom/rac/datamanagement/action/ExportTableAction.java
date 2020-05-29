package com.custom.rac.datamanagement.action;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Widget;

import com.custom.rac.datamanagement.util.AbstractTableAction;
import com.custom.rac.datamanagement.util.WriteDataToExcel;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;
import com.teamcenter.rac.util.MessageBox;

public class ExportTableAction extends AbstractTableAction {

	public ExportTableAction(ExcelTableViewPart tableViewPart) {
		super(tableViewPart);
	}

	@Override
	public void run(Widget widget) throws Exception {
		// 先判断程序是否在进行中
		tableViewPart.setExecuting(false);
		boolean isExecuting = tableViewPart.isExecuting();
		if (isExecuting) {
			throw new Exception("程序执行中，无法导出数据！");
		}
		// 文件打开路径
		String lastSelectedFilePath = OpenFileAction.lastSelectFile;
		// 设置另存对话框
		FileDialog fd = new FileDialog(tableViewPart.getContainer().getShell(), SWT.SAVE);
		fd.setFilterPath(System.getProperty("JAVA.HOME"));
		fd.setFilterExtensions(new String[] { "*.xlsx" });
		fd.setFilterNames(new String[] { "Excel Files(*.xlsx)" });
		// 设置另存文件名
		if (lastSelectedFilePath == null) {
			throw new Exception("没有数据导出！");
		}
		String fileName = lastSelectedFilePath.substring(lastSelectedFilePath.lastIndexOf("\\") + 1);
		fd.setFileName(fileName);
		String filePath = fd.open();
		if (filePath != null) {
			WriteDataToExcel.WriteData(tableViewPart, lastSelectedFilePath, filePath);
			MessageBox.post("导出成功，导出路径：" + filePath, "提示", MessageBox.INFORMATION);
		}
	}

}
