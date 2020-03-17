package com.custom.rac.datamanagement.driver;

import org.eclipse.swt.widgets.Display;

import com.custom.rac.datamanagement.util.RunState;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;

public class ExcelTableViewPartImportDriver implements IImportDriver{

	private int startRowNum;
	private ExcelTableViewPart tableViewPart;
	
	public ExcelTableViewPartImportDriver(ExcelTableViewPart tableViewPart, int startRowNum){
		this.tableViewPart = tableViewPart;
		this.startRowNum = startRowNum;
	}
	
	public ExcelTableViewPart getTableViewPart() {
		return tableViewPart;
	}
	
	@Override
	public void onSingleStart(int index) {
		System.out.println("驱动界面上的数据(开始)：" + index);
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				tableViewPart.getSWTWorkbook().getSelectedSheet().setState(index+startRowNum, RunState.running);
			}
		});
	}

	@Override
	public void onSingleFinish(int index) {
		System.out.println("驱动界面上的数据(完成)：" + index);
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				tableViewPart.setProgressValue(index+startRowNum);
				tableViewPart.getSWTWorkbook().getSelectedSheet().setState(index+startRowNum, RunState.finish);
				tableViewPart.getSWTWorkbook().getSelectedSheet().setInfomation(index+startRowNum, "导入完成");
			}
		});
		
	}

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
	}

	@Override
	public void onStart() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				tableViewPart.initProgressBar();
			}
		});
	}

	@Override
	public void onFinish() {
		
	}

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) {
	}

	@Override
	public void onSetPropertyError(int index, String propertyDisplayName, Exception e) {
		
	}

	@Override
	public void onNewItemId(int index, String itemId) {
		
	}

	@Override
	public void onNewItemRevId(int index, String itemId) {
		
	}

}
