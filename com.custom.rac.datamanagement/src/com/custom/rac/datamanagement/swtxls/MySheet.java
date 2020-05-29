package com.custom.rac.datamanagement.swtxls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySheet {

	public String name;
	public List<MyRow> rows = new ArrayList<>();
	
	public MySheet(String name) {
		this.name = name;
	}
	public void addRow(MyRow row) {
		rows.add(row);
	}
	
	public MyRow getRow(int index) {
		return rows.get(index);
	}
	
	public int getColumnNum() {
		int num = 0;
		for (MyRow myRow : rows) {
			if(myRow.cells.size() > num) {
				num = myRow.cells.size();
			}
		}
		return num;
	}
	
	/**
	 * -获取结构化的数据
	 * @param titleRowNum 标题所在行，标题以下的就是属性数据
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getStructurationData(int titleRowNum) throws Exception{
		
		List<Map<String, String>> list = new ArrayList<>();
		MyRow titleRow = rows.get(titleRowNum);
		for(int i = titleRowNum+1; i < rows.size(); i++) {
			Map<String, String> map = new HashMap<>();
			int count = rows.get(i).cells.size() > titleRow.cells.size() ? titleRow.cells.size() : rows.get(i).cells.size();
			for(int j = 0; j < count; j++) {
				map.put(titleRow.cells.get(j).value, rows.get(i).cells.get(j).value);
			}
			list.add(map);
		}
		return list;
	}
}
