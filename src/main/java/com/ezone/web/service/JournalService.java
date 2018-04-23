package com.ezone.web.service;

import java.util.List;

import javax.annotation.Resource;

import org.coody.framework.context.annotation.CacheWrite;
import org.coody.framework.context.entity.Pager;
import org.coody.framework.context.entity.Where;
import org.coody.framework.core.jdbc.JdbcHandle;
import org.coody.framework.util.HTMLSpirit;
import org.coody.framework.util.PropertUtil;
import org.coody.framework.util.SpringContextHelper;
import org.coody.framework.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.ezone.web.domain.JournalInfo;
import com.ezone.web.schema.JournalSchema;
import com.ezone.web.schema.TypeSchema;

@Service
public class JournalService {

	@Resource
	JdbcHandle jdbcHandle;
	@Resource
	TypeService typeService;
	
	@CacheWrite(validTime=7200)
	public Integer getCount(Integer typeId){
		String sql="select count(*) from journal_info where typeId=?";
		return jdbcHandle.getCount(sql, typeId);
	}
	
	public Long writeJournal(JournalInfo journal){
		return jdbcHandle.insert(journal);
	}
	
	public Long addViews(Integer id,Integer views){
		String sql="update journal_info set views=views+? where id=? limit 1";
		Long code=jdbcHandle.doUpdate(sql,views,id);
		if(code<1){
			return code;
		}
		JournalService journalService=SpringContextHelper.getBean(JournalService.class);
		if(journalService==null){
			return code;
		}
		JournalSchema journal=journalService.loadJournalInfo(id);
		if(journal==null){
			return code;
		}
		if(journal.getViews()==null){
			journal.setViews(0);
		}
		journal.setViews(journal.getViews()+views);
		return code;
	}
	
	@CacheWrite(validTime=360)
	public Pager loadJournalPager(Integer currPager,Integer typeId){
		Where where=new Where();
		if(!StringUtil.isNullOrEmpty(typeId)){
			where.set("typeId", typeId);
		}
		if(currPager==null||currPager<1){
			currPager=1;
		}
		Pager pager=new Pager(13,currPager);
		pager=jdbcHandle.findFieldPager(JournalInfo.class,"id", where, pager);
		List<JournalInfo> journalTemps=pager.getData();
		if(StringUtil.isNullOrEmpty(journalTemps)){
			return null;
		}
		List<Integer> ids=PropertUtil.getFieldValues(journalTemps, "id");
		List<JournalInfo> journals=jdbcHandle.findBean(JournalInfo.class, "id", ids,"id",true);
		if(!StringUtil.isNullOrEmpty(journals)){
			List<JournalSchema> schemas=PropertUtil.getNewList(journals, JournalSchema.class);
			for(JournalSchema schema:schemas){
				if(!StringUtil.isNullOrEmpty(schema.getHtml())){
					schema.setHtml(HTMLSpirit.delHTMLTag(schema.getHtml()));
					if(schema.getHtml().length()>420){
						schema.setHtml(schema.getHtml().substring(0,400));
					}
					pushJournalType(schema);
				}
				schema.setContext(null);
			}
			pager.setData(schemas);
		}
		return pager;
	}
	
	private void pushJournalType(JournalSchema journal){
		List<TypeSchema> types=typeService.loadTypes();
		if(StringUtil.isNullOrEmpty(types)){
			return;
		}
		for(TypeSchema type:types){
			for(TypeSchema child:type.getChilds()){
				if(journal.getTypeId()!=child.getId().intValue()){
					continue;
				}
				journal.setType(child);
			}
		}
		
	}
	
	@CacheWrite(validTime=3600)
	public List<JournalSchema> loadNews(){
		Pager pager=new Pager(46,1);
		List<JournalInfo> journals=jdbcHandle.findBean(JournalInfo.class, new Where(),pager,"id", true);
		if(StringUtil.isNullOrEmpty(journals)){
			return null;
		}
		List<JournalSchema> schemas=PropertUtil.getNewList(journals, JournalSchema.class);
		for(JournalSchema schema:schemas){
			schema.setContext(null);
		}
		return schemas;
	}
	
	@CacheWrite(validTime=3600)
	public List<JournalSchema> loadHots(){
		Pager pager=new Pager(46,1);
		List<JournalInfo> journals=jdbcHandle.findBean(JournalInfo.class, new Where(),pager,"views", true);
		if(StringUtil.isNullOrEmpty(journals)){
			return null;
		}
		List<JournalSchema> schemas=PropertUtil.getNewList(journals, JournalSchema.class);
		for(JournalSchema schema:schemas){
			schema.setContext(null);
		}
		return schemas;
	}
	
	@CacheWrite(validTime=60)
	public JournalSchema loadJournalInfo(Integer id){
		JournalInfo journalInfo= jdbcHandle.findBeanFirst(JournalInfo.class, "id", id);
		if(StringUtil.isNullOrEmpty(journalInfo)){
			return null;
		}
		JournalSchema schema=new JournalSchema();
		BeanUtils.copyProperties(journalInfo, schema);
		pushJournalType(schema);
		return schema;
	}
	public JournalInfo loadJournalInfo(String title){
		return jdbcHandle.findBeanFirst(JournalInfo.class, "title", title);
	}
	
	
	public Long deleteJournal(Integer id){
		String sql="delete from journal_info where id=? limit 1";
		return jdbcHandle.doUpdate(sql,id);
	}
	public List<JournalInfo> getJournalList(Integer lastId){
		String sql="select id,typeId,title,time from journal_info where id>? order by id limit 400 ";
		return jdbcHandle.queryAuto(JournalInfo.class, sql, lastId);
	}

	
}
