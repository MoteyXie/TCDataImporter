package com.custom.rac.datamanagement.swtxls;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.custom.rac.datamanagement.util.RunState;

import swing2swt.layout.BorderLayout;

public class SWTSheet extends TabItem implements ISWTObject{

	private DecimalFormat decimalFormat = new DecimalFormat();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/M/dd HH:mm");
	private MySheet sheet;
	private Table table;
	
	public SWTSheet(TabFolder parent, int style) {
		super(parent, style);
		checkSubclass();
	}
	
	public MySheet getSheet() {
		return sheet;
	}
	
	public Table getTable() {
		return table;
	}
	
	public void setSheet(MySheet sheet) {
		this.sheet = sheet;
		setText(sheet.name);
	}
	
	public void load(MySheet sheet) {
		this.sheet = sheet;
		setText(sheet.name);
		
		table = new Table(getParent(), SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER | SWT.VIRTUAL);
		setControl(table);
		
		table.setLayoutData(BorderLayout.CENTER);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
//		table.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		final int rowNum = sheet.rows.size();
		
		MyRow firstRow = sheet.getRow(0);
		
		int columnNum = sheet.getColumnNum();
		
		TableColumn tableColumn0 = new TableColumn(table, SWT.BORDER_SOLID | SWT.CENTER);
		tableColumn0.setWidth(70);
		
		TableColumn tableColumn1 = new TableColumn(table, SWT.BORDER_SOLID | SWT.CENTER);
		tableColumn1.setWidth(30);
		tableColumn1.setText("-");
		
		TableColumn[] columns = new TableColumn[columnNum];
		//表头
		//第一列用来做垂直编号, 第二列用来做状态标识，因此excel表的0列对应这里的2列
		for(int j = 2; j < columnNum + 2; j++) {
			
			TableColumn tableColumn = new TableColumn(table, SWT.BORDER_SOLID | SWT.CENTER);
			tableColumn.setText(j-1+"");
//			tableColumn.setWidth(sheet.getColumnWidth(j-2)/20);
			tableColumn.setWidth(120);
			
			columns[j - 2] = tableColumn;
		}
		
		int[] columnMaxWidth = new int[columnNum];
		//内容
		for(int k = 0; k < rowNum; k++) {
			
			MyRow row = sheet.getRow(k);
			
			if(row == null)continue;
			
			int cellNum = row.cells.size();
			String[] values = new String[cellNum+2];
			values[0] = (k + 1) + "";	//当列序号
			for(int m = 0; m < cellNum; m++) {
				MyCell cell = row.getCell(m);
				values[m+2] = cell.value;
				
				if(columnMaxWidth[m] == 0 || cell.value.length() > columnMaxWidth[m]) {
					columnMaxWidth[m] = cell.value.length();
				}
			}
			
			TableItem ti = new TableItem(table, SWT.NONE);
//			ti.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
			ti.setData(row);
			ti.setText(values);
			
//			ti.setImage(1, getStateImage(RunState.waitting));
		}
		
		//设置列宽
		for(int j = 0; j < columns.length; j++) {
			int width = columnMaxWidth[j] * 20;
			columns[j].setWidth(width > 300 ? 300 : width);
		}
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//右键菜单
				if (e.button == 3) {
					Menu menu = new Menu(table);
					table.setMenu(menu);
					MenuItem item = new MenuItem(menu, SWT.PUSH);
					item.setText("复制");
					item.addListener(SWT.Selection, new Listener() {
						@Override
						public void handleEvent(org.eclipse.swt.widgets.Event paramEvent) {
							
							TableItem[] selections = table.getSelection();
							
							if(selections == null || selections.length == 0)return;
							
							final TableItem selection = selections[0];
							
							int i = 0;
							for(; i < table.getColumnCount(); i++) {
								Rectangle rect = selection.getBounds(i);
								if(rect.contains(e.x, e.y)) {
									break;
								}
							}
							final int selectedCol = i;
							String text = selection.getText(selectedCol);
							Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
							// 封装文本内容
					        Transferable trans = new StringSelection(text);
					        // 把文本内容设置到系统剪贴板
					        clipboard.setContents(trans, null);
						}
					});
				}
			}
		});
		
	}
	
	public void setState(int rowNum, RunState state) {
		table.getItem(rowNum).setImage(1, getStateImage(state));
	}
	
	public void setInfomation(int rowNum, String infomation) {
		int columnNum = sheet.getColumnNum() + 1;
		table.getItem(rowNum).setText(columnNum, infomation);
	}
	
	public Image getStateImage(RunState state) {
		String imageName = "";
		if(state == RunState.waitting) {
			imageName = "Blank_Normal_16.png";
		}else if(state == RunState.running) {
			imageName = "Blank_Pressed_16.png";
		}else if(state == RunState.finish) {
			imageName = "Yes_Pressed_16.png";
		}else if(state == RunState.error) {
			imageName = "Cancel_Disable_16.png";
		}else {
			imageName = "Blank_Over_16.png";
		}
		
		return getImage(getDisplay(), imageName);
	}
	
	public Image getImage(Display display, String imgName) {
		InputStream is = this.getClass().getResourceAsStream("/resources/"+imgName);
		return is == null ? null : new Image(display,  is);
	}
	
	public String getCellValue(XSSFCell cell) {
		if(cell == null)return "";
        switch (cell.getCellType()) {
            case XSSFCell.CELL_TYPE_BLANK:
                return "";
            case XSSFCell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
            case XSSFCell.CELL_TYPE_ERROR:
                return ErrorEval.getText(cell.getErrorCellValue());
            case XSSFCell.CELL_TYPE_FORMULA:
            	try {
                    return String.valueOf(cell.getNumericCellValue());    
                } catch (IllegalStateException e) {
                	try {
                		return String.valueOf(cell.getRichStringCellValue());
                	}catch(Exception e2) {
                		
                	}
                    
                }
                return cell.getCellFormula();
            case XSSFCell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return dateFormat.format(cell.getDateCellValue());
                }else {
//                	String value = cell.getNumericCellValue() + "";
//                	return value.contains("E") ? decimalFormat.format(cell.getNumericCellValue()) : value;
                	return decimalFormat.format(cell.getNumericCellValue()).replace(",", "");
                }
                
            case XSSFCell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().toString();
            default:
                return "Unknown Cell Type: " + cell.getCellType();
        }
    }
	
	public void checkSubclass() {}

}
