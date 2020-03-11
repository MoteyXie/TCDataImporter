package com.custom.rac.datamanagement.config;

import java.util.ArrayList;

public class XMLImporters extends XMLObject {

	protected ArrayList<XMLImporter> importers;

	public ArrayList<XMLImporter> getImporters() {
		return importers;
	}

	public void setImporters(ArrayList<XMLImporter> importers) {
		this.importers = importers;
	}
	
	
}
