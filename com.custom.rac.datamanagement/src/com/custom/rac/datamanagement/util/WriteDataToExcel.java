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
		Table table = swtSheet.getTable();// ��ȡ������
		int tcol = table.getColumnCount();// ��ȡ�������
		int trow = table.getItemCount();// ��ȡ�������

		MySheet mySheet = swtSheet.getSheet();
		String name = mySheet.name;
		int scol = mySheet.getColumnNum();
		input = new FileInputStream(lastSelectedFilePath);
		XSSFWorkbook wb = new XSSFWorkbook(input);
		XSSFSheet sheet = wb.getSheet(name);
		int rowNum = sheet.getLastRowNum() + 1;// ��ȡexcel��������
		XSSFRow row = sheet.getRow(0);
		XSSFCell cell = row.createCell(scol);
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // �±߿�
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// ��߿�
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// �ϱ߿�
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// �ұ߿�
		cellStyle.setWrapText(true);
		sheet.setColumnWidth(scol, 256 * 50 + 184);// �����п�
		cell.setCellStyle(cellStyle);
		for (int i = 1; i < trow; i++) {
			TableItem tableItem = table.getItem(i);// ��ȡ��i������
			row = sheet.createRow(i);
			for (int j = 2; j < tcol; j++) {
				String value = tableItem.getText(j);// ��ȡ��j������
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
