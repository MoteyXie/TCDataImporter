package com.custom.rac.datamanagement.config;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

public class XMLConfig extends XMLObject{

	public static XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
	static{
		System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
		xstream.alias("config", XMLConfig.class);
		
		xstream.alias("importers", XMLImporters.class);
		xstream.alias("actions", XMLActions.class);
		xstream.alias("importer", XMLImporter.class);
		xstream.alias("action", XMLAction.class);
		
		xstream.aliasAttribute(XMLObject.class, "id", "id");
		
		xstream.addImplicitCollection(XMLImporters.class, "importers");
		xstream.addImplicitCollection(XMLActions.class, "actions");
	}
	
	private static XMLConfig configCache;
	
	public XMLImporters importers;
	public XMLActions actions;
	
	public static void main(String[] args) {
		XMLConfig config = load();
		System.out.println(config.getXMLString());
	}
	
	public static XMLConfig load() {
		if(configCache == null) {
			configCache = (XMLConfig) xstream.fromXML(
					XMLConfig.class.getResourceAsStream("/config.xml"));
		}
		return configCache;
	}
	
	public String getXMLString() {
		return xstream.toXML(this);
	} 
	
	@Override
	public String toString() {
		return getXMLString();
	}
}
