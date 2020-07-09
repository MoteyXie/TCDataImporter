package com.custom.rac.datamanagement.action;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Widget;

import com.custom.rac.datamanagement.driver.ExcelTableViewPartImportDriver;
import com.custom.rac.datamanagement.driver.IImportDriver;
import com.custom.rac.datamanagement.swtxls.MySheet;
import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.AbstractTableAction;
import com.custom.rac.datamanagement.util.IImporter;
import com.custom.rac.datamanagement.util.ImporterReader;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;
import com.teamcenter.rac.util.MessageBox;

public class ImportAction extends AbstractTableAction {
	// public static Thread currentThread = null;
	public static Object o = new Object();
	private static Thread currentThread = null;

	public ImportAction(ExcelTableViewPart tableViewPart) {
		super(tableViewPart);
	}

	public static ExcelTableViewPart getViewPart() {
		return tableViewPart;
	}

	@Override
	public void run(Widget widget) throws Exception {

		// 如果单击停止按钮，运行新线程
		if (AbstractImporter.exit.equals("")) {
			
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

			currentThread = new Thread() {
				public void run() {
					synchronized (o) {
						try {
							importer.execute();
						} catch (Exception e) {
							MessageBox.post(e.toString(), "错误", MessageBox.ERROR);
							e.printStackTrace();
						}
					}

				}
			};
			currentThread.start();
		}

		// 如果单击暂停按钮，启用当前线程
		else if (AbstractImporter.exit.equals("暂停")) {
			AbstractImporter.exit = "";
			synchronized (o) {
				o.notify();
			}
		}

		else if (AbstractImporter.exit.equals("停止")) {
			MessageBox.post("已停止该程序的运行，请重新加载或重载", "提示", MessageBox.INFORMATION);
		}
	}

	public static Thread getThread() {
		return currentThread;
	}

}
