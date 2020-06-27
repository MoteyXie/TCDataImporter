package com.custom.rac.datamanagement.swtxls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class SWTWorkbook extends TabFolder implements ISWTObject{

	private MyTable mytable;
	
	public SWTWorkbook(Composite parent, int style) {
		super(parent, style);
		checkSubclass();
	}
	
	public MyTable getWorkbook() {
		return mytable;
	}
	
	public void load(MyTable mytable) {
		this.mytable = mytable;
		
		for (MySheet mySheet : mytable.sheets.values()) {
			SWTSheet swtSheet = new SWTSheet(this, SWT.NONE);
			swtSheet.load(mySheet);
			swtSheet.setData(mySheet);
		}
		
		layout(true);
	}
	
	public MySheet getSelectedSheetData() {
		return (MySheet) getSelection()[0].getData();
	}
	
	public SWTSheet getSelectedSheet() {
		return (SWTSheet) getSelection()[0];
	}
	
	public SWTSheet[] getSheets() {
		int itemCount = getItemCount();
		SWTSheet[] sheets = new SWTSheet[itemCount];
		TabItem[] items = getItems();
		for(int i = 0; i < items.length; i++) {
			sheets[i] = (SWTSheet) items[i];
		}
		return sheets;
	}
	
	public void checkSubclass() {
		
	}
}
