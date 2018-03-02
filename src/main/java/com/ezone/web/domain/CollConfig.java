package com.ezone.web.domain;

import org.coody.framework.context.base.BaseModel;

@SuppressWarnings("serial")
public class CollConfig extends BaseModel{
	
	private Integer id;

	private Integer typeId;
	
	private Integer collPage;
	
	private Integer totalPage;

	public Integer getTypeId() {
		return typeId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getCollPage() {
		return collPage;
	}

	public void setCollPage(Integer collPage) {
		this.collPage = collPage;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	
	
	
}
