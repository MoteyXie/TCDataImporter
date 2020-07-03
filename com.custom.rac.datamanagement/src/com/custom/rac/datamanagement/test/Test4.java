package com.custom.rac.datamanagement.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.custom.rac.datamanagement.swtxls.ExcelEventParser;
import com.custom.rac.datamanagement.swtxls.MyCell;
import com.custom.rac.datamanagement.swtxls.MyRow;
import com.custom.rac.datamanagement.swtxls.MySheet;
import com.custom.rac.datamanagement.swtxls.MyTable;

public class Test4 {
	
	public static void main(String[] args) throws Exception {
		
		ExcelEventParser eep = new ExcelEventParser("C:\\Users\\Administrator\\Desktop\\BOM历史数据\\197-BOM导出.xlsx");
		
		MyTable myTable = eep.parse();
		
		//解析成BOM结构
		for (Entry<String, MySheet> entry : myTable.sheets.entrySet()) {
			
			System.out.println(entry.getKey());
			
			System.out.println("------------------------------------------");
			MySheet mySheet = entry.getValue();
			
			System.out.println("一共有 " + mySheet.getRowNum() + " 行数据");
			
			ComponentLoader.init(mySheet.rows.get(0));
			
			Set<String> bomDesc = new HashSet<>(mySheet.getRowNum());

			int num = 0;
			for(int i = 1; i < mySheet.getRowNum(); i++) {
				MyRow row = mySheet.getRow(i);
				
				Component component = ComponentLoader.load(row);
				
				String str = component.parentId + "-" + component.id;
				
				if(bomDesc.contains(str)) {
					System.out.println("重复的BOM行["+num+"]：" + str);
					num++;
				}else {
					bomDesc.add(str);
				}
				
			}
			
			System.out.println("共有 " + num + "个重复项");
			
		}
	}

}



	

