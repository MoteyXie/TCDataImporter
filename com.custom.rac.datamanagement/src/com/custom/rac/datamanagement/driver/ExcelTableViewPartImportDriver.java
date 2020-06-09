package com.custom.rac.datamanagement.driver;

import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.custom.rac.datamanagement.swtxls.MyCell;
import com.custom.rac.datamanagement.swtxls.SWTSheet;
import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.IImporter;
import com.custom.rac.datamanagement.util.MyCharNumber;
import com.custom.rac.datamanagement.util.RunState;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;

public class ExcelTableViewPartImportDriver implements IImportDriver{

	private int startRowNum;
	private ExcelTableViewPart tableViewPart;
	private AbstractImporter importer;
	
	public ExcelTableViewPartImportDriver(ExcelTableViewPart tableViewPart, int startRowNum){
		this.tableViewPart = tableViewPart;
		this.startRowNum = startRowNum;
	}
	
	public ExcelTableViewPart getTableViewPart() {
		return tableViewPart;
	}
	
	public abstract class MyRunnable implements Runnable{
		public int index;
		public String message;
	}
	
	private MyRunnable onSingleStartRunnable = new MyRunnable() {
		@Override
		public void run() {
			tableViewPart.getSWTWorkbook().getSelectedSheet().setState(index+startRowNum, RunState.running);
		}
	};
	
	@Override
	public void onSingleStart(int index) {
		System.out.println("驱动界面上的数据(开始)：" + index);
//		Display.getDefault().asyncExec(new Runnable() {
//			@Override
//			public void run() {
//				tableViewPart.getSWTWorkbook().getSelectedSheet().setState(index+startRowNum, RunState.running);
//			}
//		});
		onSingleStartRunnable.index = index;
		Display.getDefault().asyncExec(onSingleStartRunnable);
	}
	
	private MyRunnable onSingleFinishRunnable = new MyRunnable() {
		@Override
		public void run() {
			tableViewPart.setProgressValue(index+startRowNum);
			tableViewPart.getSWTWorkbook().getSelectedSheet().setState(index+startRowNum, RunState.finish);
			tableViewPart.getSWTWorkbook().getSelectedSheet().setInfomation(index+startRowNum, "导入完成");
			tableViewPart.getSWTWorkbook().getSelectedSheet().getTable().setSelection(index+startRowNum);
		}
		
	};

	@Override
	public void onSingleFinish(int index) {
		System.out.println("驱动界面上的数据(完成)：" + index);
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				tableViewPart.setProgressValue(index+startRowNum);
				tableViewPart.getSWTWorkbook().getSelectedSheet().setState(index+startRowNum, RunState.finish);
				tableViewPart.getSWTWorkbook().getSelectedSheet().setInfomation(index+startRowNum, "导入完成");
				tableViewPart.getSWTWorkbook().getSelectedSheet().getTable().setSelection(index+startRowNum);
			}
		});
//		onSingleFinishRunnable.index = index;
//		Display.getDefault().asyncExec(onSingleFinishRunnable);
		
	}

	private MyRunnable onSingleErrorRunnable = new MyRunnable() {
		@Override
		public void run() {
			tableViewPart.setProgressValue(index+startRowNum);
			tableViewPart.getSWTWorkbook().getSelectedSheet().setState(index+startRowNum, RunState.error);
			tableViewPart.getSWTWorkbook().getSelectedSheet().setInfomation(index+startRowNum, message);
		}
		
	};
	
	@Override
	public void onSingleError(int index, Exception e) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				tableViewPart.setProgressValue(index+startRowNum);
				tableViewPart.getSWTWorkbook().getSelectedSheet().setState(index+startRowNum, RunState.error);
				tableViewPart.getSWTWorkbook().getSelectedSheet().setInfomation(index+startRowNum, e.getMessage());
			}
		});
//		onSingleErrorRunnable.index = index;
//		onSingleErrorRunnable.message = e.getMessage();
//		Display.getDefault().asyncExec(onSingleErrorRunnable);
	}

	@Override
	public void onStart() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				tableViewPart.initProgressBar();
				tableViewPart.setExecuting(true);
			}
		});
	}

	@Override
	public void onFinish() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				tableViewPart.setExecuting(false);
			}
		});
	}

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) {
	}

	@Override
	public void onSetPropertyError(int index, String propertyDisplayName, Exception e) {
		
	}

	@Override
	public void onNewItemId(int index, String itemId) {
		setValueWithRealName(index, "item_id", itemId);
	}
	

	@Override
	public void onNewItemRevId(int index, String itemRevId) {
		setValueWithRealName(index, "item_revision_id", itemRevId);
	}

	public void setValueWithRealName(int index, String propRealName, String propValue) {
		
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				SWTSheet swtSheet = tableViewPart.getSWTWorkbook().getSelectedSheet();
				Table table = swtSheet.getTable();
				TableItem tableItem = table.getItem(index + startRowNum);
				List<MyCell> titleCells = swtSheet.getSheet().rows.get(startRowNum-1).cells;
				try {
					String displayName = importer.getDisplayNameFromRealName(index, propRealName);
					int columnNum = -1;
					for (MyCell titleCell : titleCells) {
						if(titleCell != null && titleCell.value.equals(displayName)) {
							columnNum = new MyCharNumber(titleCell.cellReference).getValue();
							break;
						}
					}
					if(columnNum > 0) {
						//cell中获取的列号是从1开始的，所以要先-1，然后加上前两列表格固定显示
						tableItem.setText(columnNum - 1 + 2, propValue);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	@Override
	public void setImporter(IImporter importer) {
		this.importer = (AbstractImporter) importer;
	}

	@Override
	public void onSingleMessage(int index, String msg) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				tableViewPart.setProgressValue(index+startRowNum);
				tableViewPart.getSWTWorkbook().getSelectedSheet().setState(index+startRowNum, RunState.finish);
				tableViewPart.getSWTWorkbook().getSelectedSheet().setInfomation(index+startRowNum, msg);
			}
		});
		
	}

	@Override
	public void onNewItemRevDesc(int index, String itemDesc) {  
		setValueWithRealName(index, "object_desc", itemDesc);		
	}

}
