package com.custom.rac.datamanagement.action;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Widget;

import com.custom.rac.datamanagement.driver.ExcelTableViewPartImportDriver;
import com.custom.rac.datamanagement.driver.IImportDriver;
import com.custom.rac.datamanagement.swtxls.MySheet;
import com.custom.rac.datamanagement.util.AbstractTableAction;
import com.custom.rac.datamanagement.util.IImporter;
import com.custom.rac.datamanagement.util.ImporterReader;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;
import com.custom.rac.itemcode.util.ImportThread;
import com.teamcenter.rac.util.MessageBox;

public class ImportAction2 extends AbstractTableAction {
	public static Object o = new Object();
	private static ImportThread thread;

	public ImportAction2(ExcelTableViewPart tableViewPart) {
		super(tableViewPart);
	}

	@Override
	public void run(Widget widget) throws Exception {

		ImporterReader importerReader = tableViewPart.importerReader;

		String selection = tableViewPart.importerSelecter.getText();

		if (selection == null || selection.length() < 1) {
			throw new Exception("必须选择一个导入程序");
		}

		IImporter importer = importerReader.getImporter(selection);

		MySheet mySheet = tableViewPart.getSWTWorkbook().getSelectedSheetData();

		int titleRowNum = 0;

		List<Map<String, String>> structruationData = mySheet.getStructurationData(titleRowNum);
		importer.loadData(structruationData);

		IImportDriver driver = new ExcelTableViewPartImportDriver(tableViewPart, titleRowNum + 1);
		importer.loadDriver(driver);
		if (thread == null) {
			thread.setRunState("");
			thread = new ImportThread(importer);
			thread.start();
		} else if (thread.getRunState().equals("停止")) {
			thread.setRunState("");
			MessageBox.post("已停止该程序的运行，请重新加载或重载", "提示", MessageBox.INFORMATION);
		}
		// 如果单击暂停按钮，启用当前线程
		else if (thread.getRunState().equals("暂停")) {
			thread.setRunState("");
			synchronized (o) {
				o.notify();
			}
		}

	}

	public static void setThread(ImportThread currentThread) {
		thread = currentThread;
	}

	public static ImportThread getThread() {
		return thread;
	}

}
