package com.ezone.web.schema;

import org.coody.framework.util.GZIPUtils;
import org.coody.framework.util.StringUtil;

import com.ezone.web.domain.JournalInfo;

@SuppressWarnings("serial")
public class JournalSchema extends JournalInfo{

	private String html;
	
	private Integer logo=StringUtil.getRanDom(1, 89);
	
	private TypeSchema type;
	

	public TypeSchema getType() {
		return type;
	}

	public void setType(TypeSchema type) {
		this.type = type;
	}

	public String getHtml() {
		if(StringUtil.isNullOrEmpty(html)){
			try {
				html=new String(GZIPUtils.uncompress(getContext()),"UTF-8");
			} catch (Exception e) {
			}
		}
		return html;
	}

	public Integer getLogo() {
		return logo;
	}

	public void setLogo(Integer logo) {
		this.logo = logo;
	}

	public void setHtml(String html) {
		this.html = html;
	}
	
	
}
