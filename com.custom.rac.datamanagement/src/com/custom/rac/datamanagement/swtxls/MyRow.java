package com.custom.rac.datamanagement.swtxls;

import java.util.ArrayList;
import java.util.List;

public class MyRow {
	
	int num;
	
	public MyRow(int num) {
		this.num = num;
	}

	public List<MyCell> cells = new ArrayList<>();
	
	public MyCell getCell(int index) {
		return cells.get(index);
	}
}
