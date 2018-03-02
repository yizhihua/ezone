package com.ezone.web.service;

import java.util.List;

import javax.annotation.Resource;

import org.coody.framework.context.annotation.CacheWrite;
import org.coody.framework.context.entity.Where;
import org.coody.framework.core.jdbc.JdbcHandle;
import org.coody.framework.util.PropertUtil;
import org.coody.framework.util.StringUtil;
import org.springframework.stereotype.Service;

import com.ezone.web.domain.TypeInfo;
import com.ezone.web.schema.TypeSchema;

@Service
public class TypeService{

	@Resource
	JdbcHandle jdbcHandle;
	@Resource
	JournalService journalService;
	
	
	@CacheWrite(validTime=600)
	public List<TypeSchema> loadTypes(){
		List<TypeInfo> types=jdbcHandle.findBean(TypeInfo.class);
		if(StringUtil.isNullOrEmpty(types)){
			return null;
		}
		List<TypeSchema> schemas=getChilds(0, types);
		return schemas;
	}
	
	private List<TypeSchema> getChilds(Integer currId,List<TypeInfo> types){
		List<TypeInfo> tmps=PropertUtil.getGroup(types, "upId", currId);
		if(StringUtil.isNullOrEmpty(tmps)){
			return null;
		}
		List<TypeSchema> schemas=PropertUtil.getNewList(tmps, TypeSchema.class);
		for(TypeSchema schema:schemas){
			List<TypeSchema> childSchemas=getChilds(schema.getId(), types);
			if(StringUtil.isNullOrEmpty(childSchemas)){
				schema.setCount(journalService.getCount(schema.getId()));
				continue;
			}
			schema.setChilds(childSchemas);
			List<Integer> counts=PropertUtil.getFieldValues(childSchemas, "count");
			schema.setCount(StringUtil.SumInteger(counts.toArray(new Integer[]{})));
		}
		return schemas;
	}
	
	public Long writeTypeInfo(TypeInfo typeInfo){
		try {
			return jdbcHandle.insert(typeInfo);
		} catch (Exception e) {
			TypeInfo newType=jdbcHandle.findBeanFirst(TypeInfo.class,"typeName",typeInfo.getTypeName());
			typeInfo.setId(newType.getId());
			jdbcHandle.saveOrUpdateAuto(newType);
			return newType.getId().longValue();
		}
	}
	
	public List<TypeInfo> getTypes(){
		Where where=new  Where();
		where.set("upId", "<>",0);
		List<TypeInfo> types=jdbcHandle.findBean(TypeInfo.class,where);
		if(StringUtil.isNullOrEmpty(types)){
			return null;
		}
		return types;
	}
}
