package com.ezone.web.service;

import java.util.List;

import javax.annotation.Resource;

import org.coody.framework.context.annotation.CacheWrite;
import org.coody.framework.core.jdbc.JdbcHandle;
import org.coody.framework.util.StringUtil;
import org.springframework.stereotype.Service;

import com.ezone.web.domain.ToolsInfo;

@Service
public class ToolsService {
	
	@Resource
	JdbcHandle jdbcHandle;

	
	@CacheWrite(time=600)
	public List<ToolsInfo> loadTools(){
		List<ToolsInfo> tools= jdbcHandle.findBean(ToolsInfo.class);
		return tools;
	}
	@CacheWrite(time=600)
	public List<ToolsInfo> loadToolsAsFormat(){
		List<ToolsInfo> tools= jdbcHandle.findBean(ToolsInfo.class);
		if(!StringUtil.isNullOrEmpty(tools)){
			for(ToolsInfo tool:tools){
				tool.setUrl("/"+tool.getUrl());
			}
		}
		return tools;
	}
	
}
