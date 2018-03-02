package com.ezone.web.coll;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.coody.framework.context.entity.HttpEntity;
import org.coody.framework.util.PropertUtil;
import org.coody.framework.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ezone.web.domain.TypeInfo;
import com.ezone.web.schema.TypeSchema;
import com.ezone.web.service.TypeService;
import com.ezone.web.util.BaiduHttpUtil;

@Component
public class CollJournalType{
	
	@Resource
	TypeService typeService;

	public static final String url = "http://www.myexception.cn/index.html";
	public static final String framePattern = "<div class=\"m_t_c_\\w+\"><span>.*?</div>(?=(</ul><div class=\"m_t_c_\\w+\">)||(</ul></div></div><div class=\"m_a_3\">))";
	public static final String columnPattern = "<a href=\"http://www.myexception.cn/\\w(.*?)\" target=\"_blank\">.*?</a>";
	BaiduHttpUtil httpUtil=new BaiduHttpUtil();
	
	// 获得首页框架大类
	public List<String> LoadFrame() {
		HttpEntity hEntity = httpUtil.Get(url);
		String html = hEntity.getHtml();
		hEntity = null;
		List<String> articeHtml =StringUtil.doMatcher(html, framePattern);
		if (articeHtml == null || articeHtml.equals("")) {
			return null;
		}
		return articeHtml;
	}
	// 获得每大框架子类
	public List<TypeSchema> loadColumn(List<String> htmlList) {
		List<TypeSchema> parentTypes = new ArrayList<TypeSchema>();
		for (String html : htmlList) {
			TypeSchema parent = new TypeSchema();
			String outlineTitle = StringUtil.textCutCenter(html, "<span>","</span>");
			List<TypeSchema> childTypes=getChildTypes(html);
			parent.setTypeName(outlineTitle);
			parent.setChilds(childTypes);
			parentTypes.add(parent);
		}
		return parentTypes;
	}
	public List<TypeSchema> getChildTypes(String html){
		List<String> outlines = StringUtil.doMatcher(html, columnPattern);
		if (StringUtil.isNullOrEmpty(outlines)) {
			return null;
		}
		List<TypeSchema> childTypes=new ArrayList<TypeSchema>();
		for (String outline : outlines) {
			String urltmp = StringUtil.textCutCenter(outline, "href=\"","\"");
			String titleTmp = StringUtil.textCutCenter(outline, ">", "<");
			if (urltmp == null || urltmp.equals("")) {
				continue;
			}
			TypeSchema child = new TypeSchema();
			child.setTypeName("_" + titleTmp);
			child.setOtherUrl(urltmp);
			childTypes.add(child);
		}
		if(StringUtil.isNullOrEmpty(childTypes)){
			return null;
		}
		return childTypes;
	}
	public void saveTypes(List<TypeSchema>  list){
		if(StringUtil.isNullOrEmpty(list)){
			return;
		}
		for(TypeSchema schema:list){
			TypeInfo typeInfo=new TypeInfo();
			BeanUtils.copyProperties(schema, typeInfo);
			Long typeId=typeService.writeTypeInfo(typeInfo);
			if(StringUtil.isNullOrEmpty(schema.getChilds())){
				continue;
			}
			List<TypeSchema> childs=schema.getChilds();
			PropertUtil.setFieldValues(childs, "upId", typeId);
			saveTypes(childs);
		}
	}
	public void collTypeInfos() {
		try {
			List<String> articeHtml =LoadFrame();
			List<TypeSchema>  list=loadColumn(articeHtml);
			saveTypes(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
}
