package com.custom.rac.datamanagement.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.custom.rac.datamanagement.swtxls.MySheet;
import com.custom.rac.datamanagement.swtxls.SWTSheet;
import com.custom.rac.datamanagement.swtxls.SWTWorkbook;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;

public class WriteDataToExcel {

	public static void writeLargeData(ExcelTableViewPart tableViewPart, String lastSelectedFilePath, String filePath)
			throws IOException {
		OutputStream out = null;

		SXSSFWorkbook swb = new SXSSFWorkbook(100);
		CellStyle cellStyle = swb.createCellStyle();

		SWTWorkbook swtWorkbook = tableViewPart.getSWTWorkbook();

		SWTSheet[] sheets = swtWorkbook.getSheets();

		for (SWTSheet swtSheet : sheets) {
			Table table = swtSheet.getTable();// 获取表格对象
			int tcol = table.getColumnCount();// 获取表格列数
			int trow = table.getItemCount();// 获取表格行数
			MySheet mySheet = swtSheet.getSheet();
			String name = mySheet.name;
			int scol = mySheet.getColumnNum();

			Sheet sheet = swb.createSheet(name);

//			List<Map<String, String>> lists = mySheet.getStructurationData(0);

			Row row = null;
			Cell cell = null;

			TableItem tableItem = table.getItem(0);// 获取第1行数据
			row = sheet.createRow(0);
			for (int j = 2; j < tcol; j++) {
				String value = tableItem.getText(j);// 获取第j列数据
				cell = row.createCell(j - 2);
				cell.setCellValue(value);
				cell.setCellStyle(cellStyle);
			}

			for (int i = 1; i < trow; i++) {
				tableItem = table.getItem(i);// 获取第i行数据
				row = sheet.createRow(i);
				for (int j = 2; j < tcol; j++) {
					String value = tableItem.getText(j);// 获取第j列数据
					cell = row.createCell(j - 2);
					cell.setCellValue(value);
					cell.setCellStyle(cellStyle);
				}
				Object state = tableItem.getData("state");
				if (state == null)
					continue;
				cell = row.createCell(tcol - 2);
				cell.setCellValue(state.toString());
				cell.setCellStyle(cellStyle);
			}
//			for (int k = trow; k < rowNum; k++) {
//				row = sheet.createRow(k);
//			}
			setAutoWidth(sheet, scol);

		}

		out = new FileOutputStream(filePath);
		swb.write(out);

		if (out != null) {
			out.close();
		}
	}

	public static void writeData(ExcelTableViewPart tableViewPart, String lastSelectedFilePath, String filePath)
			throws Exception {
		InputStream input = null;
		OutputStream out = null;
		SWTSheet swtSheet = tableViewPart.getSWTWorkbook().getSelectedSheet();
		Table table = swtSheet.getTable();// 获取表格对象
		int tcol = table.getColumnCount();// 获取表格列数
		int trow = table.getItemCount();// 获取表格行数
		MySheet mySheet = swtSheet.getSheet();
		String name = mySheet.name;
		int scol = mySheet.getColumnNum();
		input = new FileInputStream(lastSelectedFilePath);
		XSSFWorkbook wb = new XSSFWorkbook(input);
		XSSFSheet sheet = wb.getSheet(name);
		int rowNum = sheet.getLastRowNum() + 1;// 获取excel表总行数
		XSSFRow row = sheet.getRow(0);
		XSSFCell cell = row.createCell(scol);
		XSSFCellStyle cellStyle = wb.createCellStyle();

		for (int i = 1; i < trow; i++) {
			TableItem tableItem = table.getItem(i);// 获取第i行数据
			row = sheet.createRow(i);
			for (int j = 2; j < tcol; j++) {
				String value = tableItem.getText(j);// 获取第j列数据
				cell = row.createCell(j - 2);
				cell.setCellValue(value);
				cell.setCellStyle(cellStyle);
			}
			Object state = tableItem.getData("state");
			if (state == null)
				continue;
			cell = row.createCell(tcol - 2);
			cell.setCellValue(state.toString());
			cell.setCellStyle(cellStyle);
		}
		for (int k = trow; k < rowNum; k++) {
			row = sheet.createRow(k);
		}
		setAutoWidth(sheet, scol);
		out = new FileOutputStream(filePath);
		wb.write(out);
		if (input != null) {
			input.close();
		}
		if (out != null) {
			out.close();
		}
	}

	public void saveData(ExcelTableViewPart tableViewPart) {

	}

	/**
	 * 设置自动列宽
	 * 
	 * @param sheet
	 * @param columnNum
	 */
	public static void setAutoWidth(Sheet sheet, int columnNum) {

		for (int colNum = 0; colNum < columnNum; colNum++) {
			int columnWidth = sheet.getColumnWidth(colNum) / 256;
			for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
				Row currentRow;

				if (sheet.getRow(rowNum) == null) {
					currentRow = sheet.createRow(rowNum);
				} else {
					currentRow = sheet.getRow(rowNum);
				}
				if (currentRow.getCell(colNum) != null) {
					Cell currentCell = currentRow.getCell(colNum);
					if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
						int length = currentCell.getStringCellValue().getBytes().length;
						if (columnWidth < length) {
							columnWidth = length;
						}
					}
				}
			}
			sheet.setColumnWidth(colNum, (columnWidth + 4) * 128);
		}
	}
}
