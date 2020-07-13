package com.custom.rac.datamanagement.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.custom.rac.datamanagement.swtxls.ExcelEventParser;
import com.custom.rac.datamanagement.swtxls.MyCell;
import com.custom.rac.datamanagement.swtxls.MyRow;
import com.custom.rac.datamanagement.swtxls.MySheet;
import com.custom.rac.datamanagement.swtxls.MyTable;

public class Test3 {
	
	public static void main(String[] args) throws Exception {
		
		ExcelEventParser eep = new ExcelEventParser("C:\\Users\\Administrator\\Desktop\\198-BOM导出.xlsx");
		
		MyTable myTable = eep.parse();
		
		//解析成BOM结构
		
		
		for (Entry<String, MySheet> entry : myTable.sheets.entrySet()) {
			
			System.out.println(entry.getKey());
			
			System.out.println("------------------------------------------");
			MySheet mySheet = entry.getValue();
			
			System.out.println("一共有 " + mySheet.getRowNum() + " 行数据");
			
			ComponentLoader.init(mySheet.rows.get(0));
			
			Map<String, Bom> boms = new HashMap<>();
			
			List<BomDifference> bomDifs = new ArrayList<>();
			
			List<Bom> existBoms = new ArrayList<>();
			
			Bom bom = null;
			for(int i = 1; i < mySheet.getRowNum(); i++) {
				MyRow row = mySheet.getRow(i);
				
				Component component = ComponentLoader.load(row);
				
				if(bom == null) {
					bom = new Bom();
					bom.id = component.parentId;
				}
				
				if(!component.parentId.equals(bom.id)) {
					//此行和最后一行BOM父项ID不一致，说明是一个新的BOM了
					
					//bom放入集合之前需要做差异对比
					if(boms.containsKey(bom.id)) {
						Bom bom1 = boms.get(bom.id);
						BomDifference bdf = new BomDifference();
						bdf.comparison(bom1, bom);
						//记录差异
						if(bdf.hasDifference()) {
							bomDifs.add(bdf);
						}else{
							//重复却没有差异的BOM
							existBoms.add(bom);
						}
					}
					boms.put(bom.id, bom);
					
					bom = new Bom();
					bom.id = component.parentId;
					bom.components.add(component);
				}else {
					bom.components.add(component);
				}
				
				if(i == mySheet.getRowNum()-1) {
					//bom放入集合之前需要做差异对比
					if(boms.containsKey(bom.id)) {
						Bom bom1 = boms.get(bom.id);
						BomDifference bdf = new BomDifference();
						bdf.comparison(bom1, bom);
						//记录差异
						if(bdf.hasDifference()) {
							bomDifs.add(bdf);
						}else{
							//重复却没有差异的BOM
							existBoms.add(bom);
						}
					}
					boms.put(bom.id, bom);
				}
				
			
			}
			
			for (Bom b : boms.values()) {
				b.print();
			}
			
			
			//找到有差异的，保存并打印
			if(bomDifs.size() > 0) {
				
				System.out.println("以下为存在差异的BOM");
				for (BomDifference bomDif : bomDifs) {
					String str = bomDif.printString();
					
					System.out.println("---------------------------------------------");
					System.out.println(str);
				}
				
			}else {
				System.out.println("没有差异");
			}
			
			if(existBoms.size() > 0) {
				System.out.println("以下为重复且没有差异的BOM");
				for (Bom b : existBoms) {
					System.out.println("---------------------------------------------");
//					b.print();
					System.out.println(b.id);
				}
			}else {
				System.out.println("没有重复的BOM");
			}
			
		}
	}

}


class Bom {
	public String id;
	public List<Component> components = new ArrayList<Component>();
	
	public Component getComponent(String componentId) {
		for (Component component : components) {
			if(component.id.equals(componentId)) {
				return component;
			}
		}
		return null;
	}
	
	public void print() {
		StringBuilder sb = new StringBuilder();
		sb.append(id+"\n");
		for (Component component : components) {
			sb.append("|-" + component.toString()+"\n");
		}
		System.out.println(sb.toString());
	}
	
	public boolean equals(Object obj) {
		
		if(obj instanceof Bom) {
			
			Bom thatBom = (Bom) obj;
			
			//父项ID一样的情况下，对比两者的组件是否完全一致
			if(id.equals(thatBom.id)) {
				
				BomDifference bd = new BomDifference();
				try {
					bd.comparison(this, thatBom);
					if(bd.hasDifference()) {
						return false;
					}else {
						return true;
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
		}
		return false;
	}
}

class ComponentLoader {
	
	public static int parent_num = -1;
	public static int component_num = -1;
	public static int index_num = -1;
	public static int amount_num = -1;
	
	public static void init(MyRow row) {
		
		int cellNum = 0;
		for (MyCell cell : row.cells) {
			switch(cell.value) {
				case "装配件编码" : parent_num = cellNum;break;
				case "组件编码" : component_num = cellNum;break;
				case "序号" : index_num = cellNum;break;
				case "组件用量" : amount_num = cellNum;break;
			}
			cellNum++;
		}
	}
	
	public static Component load(MyRow row) {
		
		Component component = new Component();
		component.amount = row.getCell(amount_num).value;
		component.id = row.getCell(component_num).value;
		component.index = row.getCell(index_num).value;
		component.parentId = row.getCell(parent_num).value;
		return component;
		
	}
	
}

class Component{
	
	public String parentId;
	public String id;
	public String index;
	public String amount;
	
	public boolean equals(Object obj) {
	
		if(obj instanceof Component) {
			
			Component thatComponent = (Component) obj;
			
			return id.equals(thatComponent.id) && parentId.equals(thatComponent.parentId);
			
		}
	
		return false;
	}

	public int hashCode() {
		return id.hashCode() + parentId.hashCode();
	}
	
	public String toString() {
		try {
			Field[] fields = getClass().getFields();
			StringBuilder sb = new StringBuilder();
			for (Field field : fields) {
				sb.append(field.getName() + ":" + field.get(this)+", ");
			}
			String str = sb.substring(0, sb.length() - 2);
			return str + ";";
		}catch(Exception e) {
			e.printStackTrace();
		}
		return super.toString();
		
	}
	
//	public boolean equals(Object obj) {
//		
//		if(obj instanceof Component) {
//			
//			Component thatComponent = (Component) obj;
//			
//			return id.equals(thatComponent.id)
//					&& index == thatComponent.index
//					&& amount == thatComponent.amount;
//			
//		}
//		
//		return false;
//	}
//	
//	public int hashCode() {
//		return id.hashCode() + Integer.hashCode(index) + Float.hashCode(amount);
//	}
	
}

class BomDifference {
	
	public Bom bom1;
	
	public Bom bom2;
	
	public List<Component> extraComponents = new ArrayList<Component>();
	
	public List<Component> missingComponents = new ArrayList<Component>();
	
	public Map<Component, String> differenceDescs = new HashMap<>();
	
	public boolean hasDifference() {
		return differenceDescs.size() > 0 || extraComponents.size() > 0 || missingComponents.size() > 0;
	}
	
	public void comparison(Bom bom1, Bom bom2) throws Exception {
		
		extraComponents.clear();
		missingComponents.clear();
		differenceDescs.clear();
		
		this.bom1 = bom1;
		this.bom2 = bom2;
		
		if(bom1.components == null || bom1.components.size() == 0) {
			
			if(bom2.components == null || bom2.components.size() == 0) {
				return;
			}else {
				extraComponents = bom2.components;
				return;
			}
		}
		
		if(bom2.components == null || bom2.components.size() == 0) {
			
			if(bom1.components == null || bom1.components.size() == 0) {
				return;
			}else {
				missingComponents = bom1.components;
				return;
			}
		}
		
		for (Component component1 : bom1.components) {
			
			//bom2不包含这个组件，说明这个组件是被删掉的
			if(!bom2.components.contains(component1)) {
				boolean flag = component1.equals(bom2.components.get(0));
				missingComponents.add(component1);
			}
		}
		
		for (Component component2 : bom2.components) {
			//bom1不包含这个组件，说明这个组件是新增的
			if(!bom1.components.contains(component2)) {
				extraComponents.add(component2);
			//如果包含，还需要比对两个bom行的属性差异
			}else {
				Component component1 = bom1.getComponent(component2.id);
				String dif = ComponentDifference.comparison(component1, component2);
				if(dif != null && dif.length() > 0) {
					//有差异的话将差异信息放到map表
					differenceDescs.put(component2, dif);
				}
			}
		}
	}
	
	public String printString() {
		
		if(!hasDifference()) {
			return "没有差异";
		}else {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append(bom1.id + " 的差异信息：\n");
			
			if(extraComponents.size() > 0) {
				sb.append("新增的BOM行：\n");
				for (Component component : extraComponents) {
					sb.append(component.toString());
				}
				sb.append("\n");
			}
			if(missingComponents.size() > 0) {
				sb.append("删除的BOM行：\n");
				for (Component component : missingComponents) {
					sb.append(component.toString());
				}
				sb.append("\n");
			}
			if(differenceDescs.size() > 0) {
				sb.append("有差异的行：\n");
				for (Entry<Component, String> entry : differenceDescs.entrySet()) {
					sb.append(entry.getKey().id + " : " + entry.getValue());
				}
				sb.append("\n");
			}
			
			return sb.toString();
			
		}
		
	}
}

class ComponentDifference {
	
	public static String comparison(Component component1, Component component2) throws Exception {
		Field[] fields = component1.getClass().getFields();
		StringBuilder sb = new StringBuilder();
		for (Field field : fields) {
			Object obj1 = field.get(component1);
			Object obj2 = field.get(component2);
			
			if(obj1 == null || obj2 == null) {
				if(obj1 == null) {
					if(obj2 != null) {
						sb.append(field.getName() + ": null ->" + obj2+"; ");
					}
				}else {
					if(obj2 == null) {
						sb.append(field.getName() + ": " + obj1 +" -> null; ");
					}
				}
			}else if(!obj2.equals(obj1)){
				sb.append(field.getName() + ": " + obj1 +" -> " + obj2 + "; ");
			}
		}
		return sb.toString();
	}
}
