package com.ezone.web.service;

import javax.annotation.Resource;

import org.coody.framework.core.jdbc.JdbcHandle;
import org.springframework.stereotype.Service;

import com.ezone.web.domain.CollConfig;

@Service
public class CollService {

	@Resource
	JdbcHandle jdbcHandle;
	
	public CollConfig loadCollConfig(){
		return jdbcHandle.findBeanFirst(CollConfig.class);
	}
	
	public void writeCollConfig(CollConfig config){
		config.setId(1);
		jdbcHandle.saveOrUpdateAuto(config);
	}
	
	public void removeCollConfig(){
		jdbcHandle.doUpdate("delete from coll_config");
	}
}
