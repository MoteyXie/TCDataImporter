package com.custom.rac.datamanagement.swtxls;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class MyTable {

	public LinkedHashMap<String, MySheet> sheets = new LinkedHashMap<>();
	
	public int getNumberOfSheets() {
		return sheets.size();
	}
	public MySheet getSheet(String sheetName) {
		return sheets.containsKey(sheetName) ? sheets.get(sheetName) : null;
	}
	
	public void addSheet(String sheetName, MySheet sheet) throws Exception {
		if(sheets.containsKey(sheetName))
			throw new Exception("工作表【"+sheetName+"】已存在！");
		
		sheets.put(sheetName, sheet);
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		for (Entry<String, MySheet> entry : sheets.entrySet()) {
			
			sb.append("----------" + entry.getKey() + "----------\n");
			for (MyRow myRow : entry.getValue().rows) {
				for (MyCell myCell : myRow.cells) {
					sb.append(myCell.value + "\t");
				}
				sb.append("-----------\n");
			}
			
		}
		
		return sb.toString();
	}
	
}
