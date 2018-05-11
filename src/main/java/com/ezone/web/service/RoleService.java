package com.ezone.web.service;

import javax.annotation.Resource;

import org.coody.framework.context.annotation.CacheWrite;
import org.coody.framework.core.jdbc.JdbcHandle;
import org.springframework.stereotype.Service;

import com.ezone.web.constant.CacheFinal;
import com.ezone.web.domain.UserRole;

@Service
public class RoleService {

	@Resource
	JdbcHandle jdbcHandle;
	
	@CacheWrite(key=CacheFinal.USER_ROLE_INFO,time=600)
	public UserRole loadRole(Integer roleId){
		return jdbcHandle.findBeanFirst(UserRole.class,"id",roleId);
	}
}
