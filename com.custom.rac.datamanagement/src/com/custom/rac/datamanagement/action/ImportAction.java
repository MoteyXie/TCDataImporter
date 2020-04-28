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
import com.teamcenter.rac.util.MessageBox;

public class ImportAction extends AbstractTableAction{

	public ImportAction(ExcelTableViewPart tableViewPart) {
		super(tableViewPart);
	}

	@Override
	public void run(Widget widget) throws Exception {

		ImporterReader importerReader = tableViewPart.importerReader;
		
		String selection = tableViewPart.importerSelecter.getText();
		
		if(selection == null || selection.length() < 1) {
			throw new Exception("请选择一个导入程序！");
		}
		IImporter importer = importerReader.getImporter(selection);
		
		MySheet mySheet = tableViewPart.getSWTWorkbook().getSelectedSheetData();
		
		int titleRowNum = 0;
		
		List<Map<String, String>> structruationData = mySheet.getStructurationData(titleRowNum);
		importer.loadData(structruationData);
		
		IImportDriver driver = new ExcelTableViewPartImportDriver(tableViewPart, titleRowNum+1);
		importer.loadDriver(driver);
		
		new Thread() {
			public void run() {
				try {
					importer.execute();
				} catch (Exception e) {
					MessageBox.post(e.toString(),"错误",MessageBox.ERROR);
					e.printStackTrace();
				}
			}
		}.start();
		
		
	}

}
