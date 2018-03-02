package com.ezone.web.schema;

import java.util.List;

import com.ezone.web.domain.TypeInfo;

@SuppressWarnings("serial")
public class TypeSchema extends TypeInfo{
	
	private Integer count=0;

	private List<TypeSchema> childs;

	public List<TypeSchema> getChilds() {
		return childs;
	}

	public void setChilds(List<TypeSchema> childs) {
		this.childs = childs;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
	
}
