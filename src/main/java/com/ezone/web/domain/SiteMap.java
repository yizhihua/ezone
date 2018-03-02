package com.ezone.web.domain;


import java.util.Date;

import org.coody.framework.context.base.BaseModel;

/**
 * SiteMap entity. @author MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public class SiteMap extends BaseModel {

	// Fields

	private Integer id;
	private String html;
	private String xml;
	private Integer lastId;
	private String domain;
	private Date updateTime;
	private String baiduUrl;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	public Integer getLastId() {
		return lastId;
	}
	public void setLastId(Integer lastId) {
		this.lastId = lastId;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getBaiduUrl() {
		return baiduUrl;
	}
	public void setBaiduUrl(String baiduUrl) {
		this.baiduUrl = baiduUrl;
	}

}