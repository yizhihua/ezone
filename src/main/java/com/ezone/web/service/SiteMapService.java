package com.ezone.web.service;

import javax.annotation.Resource;

import org.coody.framework.context.annotation.CacheWrite;
import org.coody.framework.core.jdbc.JdbcHandle;
import org.springframework.stereotype.Service;

import com.ezone.web.domain.SiteMap;

@Service
public class SiteMapService {

	@Resource
	JdbcHandle jdbcHandle;
	
	@CacheWrite(validTime=60)
	public SiteMap loadSiteMap(){
		return jdbcHandle.findBeanFirst(SiteMap.class);
	}
	
	public SiteMap loadSiteMapNoCache(){
		return jdbcHandle.findBeanFirst(SiteMap.class);
	}
	
	public void saveSiteMap(SiteMap siteMap){
		jdbcHandle.saveOrUpdateAuto(siteMap);
	}
}
