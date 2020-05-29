package com.custom.rac.datamanagement.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;


@XStreamAlias("result") 
public class XMLResult {

	@XStreamAsAttribute
	public String value = "";
	
	@XStreamAsAttribute
	public String error = "";	
	
	@XStreamOmitField
	private static XStream xstream = new XStream(new DomDriver("GBK", new XmlFriendlyNameCoder("-_", "_")));
	static {
		xstream.alias("result", XMLResult.class);
	}
	public static XMLResult read(String xml) {
		
		return (XMLResult) xstream.fromXML(xml);
	}	
}
