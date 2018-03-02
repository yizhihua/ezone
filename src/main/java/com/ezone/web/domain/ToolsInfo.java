package com.ezone.web.domain;

import org.coody.framework.context.base.BaseModel;

/**
 * Tools entity. @author MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public class ToolsInfo extends BaseModel {

	// Fields

	private Integer id;
	private String url;
	private String filePath;
	private String title;
	private String remark;
	private String logo;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	

}