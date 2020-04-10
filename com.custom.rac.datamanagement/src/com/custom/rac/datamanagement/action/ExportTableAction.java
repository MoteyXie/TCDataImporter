package com.custom.rac.datamanagement.action;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

import com.custom.rac.datamanagement.swtxls.MySheet;
import com.custom.rac.datamanagement.swtxls.SWTSheet;
import com.custom.rac.datamanagement.util.AbstractTableAction;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;
import com.teamcenter.rac.util.MessageBox;

public class ExportTableAction extends AbstractTableAction {

	public ExportTableAction(ExcelTableViewPart tableViewPart) {
		super(tableViewPart);
	}

	@Override
	public void run(Widget widget) throws Exception {
		InputStream input = null;
		OutputStream out = null;
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

		SWTSheet swtSheet = tableViewPart.getSWTWorkbook().getSelectedSheet();
		Table table = swtSheet.getTable();// 获取表格对象
		int tcol = table.getColumnCount();// 获取表格列数
		int trow = table.getItemCount();// 获取表格行数

		MySheet mySheet = swtSheet.getSheet();
		String name = mySheet.name;
		int scol = mySheet.getColumnNum();
		try {
			input = new FileInputStream(lastSelectedFilePath);
			XSSFWorkbook wb = new XSSFWorkbook(input);
			XSSFSheet sheet = wb.getSheet(name);
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
				for (int j = 2; j < tcol; j++) {
					TableItem tableItem = table.getItem(i);// 获取第i行数据
					String value = tableItem.getText(j);// 获取第j列数据
					row = sheet.getRow(i);
					cell = row.createCell(j - 2);
					cell.setCellValue(value);
					cell.setCellStyle(cellStyle);
				}
			}

			String filePath = fd.open();
			if (filePath != null) {
				out = new FileOutputStream(filePath);
				wb.write(out);
				MessageBox.post("导出成功，导出路径：" + filePath, "提示", MessageBox.INFORMATION);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				input.close();
			}
			if (out != null) {
				out.close();
			}
		}

	}

}
