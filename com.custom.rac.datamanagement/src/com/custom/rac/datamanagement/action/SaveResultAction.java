package com.custom.rac.datamanagement.action;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Widget;

import com.custom.rac.datamanagement.util.AbstractTableAction;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;

public class SaveResultAction extends AbstractTableAction {

	public SaveResultAction(ExcelTableViewPart tableViewPart) {
		super(tableViewPart);
	}

	@Override
	public void run(Widget widget) throws Exception {

		//先判断程序是否在进行中
		boolean isExecuting = tableViewPart.isExecuting();
		if(isExecuting) {
			throw new Exception("程序执行中，无法保存数据！");
		}
		
		String lastSelectedFilePath = OpenFileAction.lastSelectFile;
		
		Table table = tableViewPart.getSWTWorkbook().getSelectedSheet().getTable();
	}

}
