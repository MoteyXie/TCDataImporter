package com.custom.rac.datamanagement.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.custom.rac.datamanagement.swtxls.ExcelEventParser;
import com.custom.rac.datamanagement.swtxls.MySheet;
import com.custom.rac.datamanagement.swtxls.MyTable;

public class MyTableTest2 {

	@Test
	public void testGetStructurationData() {
//		fail("Not yet implemented");
		try {
			ExcelEventParser eep = new ExcelEventParser("D:\\01_我的工作\\小熊电器\\01工作区\\04系统设计\\历史数据导入（测试数据）\\【小熊电器 PLM】电子件物料历史数据整理模板v1.1&nbsp; 数据20180829.xlsx");
			MyTable myTable = eep.parse();
			for (Entry<String, MySheet> entry : myTable.sheets.entrySet()) {
				System.out.println("-----" + entry.getKey() + "-----");
				MySheet mySheet = entry.getValue();
				List<Map<String, String>> structurationData = mySheet.getStructurationData(1);
				
				for (Map<String, String> map : structurationData) {
					for (Entry<String, String> entry2 : map.entrySet()) {
						System.out.print(entry2.getKey() + ":" + entry2.getValue()+"\t");
					}
					System.out.println("");
					System.out.println("-------------------------");
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
