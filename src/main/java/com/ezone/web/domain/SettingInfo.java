package com.ezone.web.domain;

import org.coody.framework.context.base.BaseModel;

/**
 * 设置 Setting entity. @author MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public class SettingInfo extends BaseModel {

	// Fields

	private Integer id;
	private String siteName;
	private String keywords;
	private String description;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public SettingInfo(){
	}

}