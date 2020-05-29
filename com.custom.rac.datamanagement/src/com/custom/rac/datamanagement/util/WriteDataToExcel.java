package com.custom.rac.datamanagement.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.custom.rac.datamanagement.swtxls.MySheet;
import com.custom.rac.datamanagement.swtxls.SWTSheet;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;

public class WriteDataToExcel {
	public static void WriteData(ExcelTableViewPart tableViewPart, String lastSelectedFilePath, String filePath)
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
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
		cellStyle.setWrapText(true);
		sheet.setColumnWidth(scol, 256 * 50 + 184);// 设置列宽
		cell.setCellStyle(cellStyle);
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
			cell = row.createCell(tcol - 2);
			cell.setCellValue(state.toString());
			cell.setCellStyle(cellStyle);
		}
		for (int k = trow; k < rowNum; k++) {
			row = sheet.createRow(k);
		}
		out = new FileOutputStream(filePath);
		wb.write(out);
		if (input != null) {
			input.close();
		}
		if (out != null) {
			out.close();
		}
	}
}
