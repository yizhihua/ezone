package com.ezone.web.domain;

import org.coody.framework.context.base.BaseModel;

@SuppressWarnings("serial")
public class TypeInfo extends BaseModel{

	
	private Integer id;
	private String typeName;
	private Integer upId;
	private String otherUrl;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Integer getUpId() {
		return upId;
	}
	public void setUpId(Integer upId) {
		this.upId = upId;
	}
	public String getOtherUrl() {
		return otherUrl;
	}
	public void setOtherUrl(String otherUrl) {
		this.otherUrl = otherUrl;
	}
	
	

}
