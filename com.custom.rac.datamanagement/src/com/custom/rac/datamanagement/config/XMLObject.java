package com.custom.rac.datamanagement.config;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class XMLObject{

	@XStreamAsAttribute
	protected String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
