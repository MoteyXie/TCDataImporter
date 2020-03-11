package com.custom.rac.datamanagement.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.custom.rac.datamanagement.config.XMLConfig;
import com.custom.rac.datamanagement.config.XMLImporter;

public class ImporterReader {

	public static final String packageName = "com.custom.rac.datamanagement.importer";
	
	public static void main(String[] args) {
		ImporterReader r = new ImporterReader();
		for (String string : r.getAllImporterName()) {
			System.out.println(string);
		}
	}
	
	Map<String, IImporter> importers = new HashMap<>();
	
	public ImporterReader() {
		try {
			load();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String[] getAllImporterName() {
		return importers.keySet().toArray(new String[importers.size()]);
	}
	
	public IImporter getImporter(String name) {
		return importers.containsKey(name) ? importers.get(name) : null;
	}
	
	public Map<String, IImporter> load() throws Exception{
		
		importers.clear();
		
//		Set<Class<?>> classes = ClassScanner.getClasses(packageName);
//		
//		if(classes.size() > 0) {
//			for (Class<?> class1 : classes) {
//				Object obj = class1.newInstance();
//				if(obj instanceof IImporter) {
//					IImporter ii = (IImporter) obj;
//					importers.put(ii.getName(), ii);
//				}
//			}
//		}
		
		XMLConfig config = XMLConfig.load();
		ArrayList<XMLImporter> xmlImporters = config.importers.getImporters();
		
		for (XMLImporter xmlImporter : xmlImporters) {
			Class<?> cls = Class.forName(xmlImporter.getId());
			try {
				IImporter ii = (IImporter) cls.newInstance();
				importers.put(ii.getName(), ii);	
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return importers;
	}
}
