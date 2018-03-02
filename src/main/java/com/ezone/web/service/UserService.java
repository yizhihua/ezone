package com.ezone.web.service;

import java.util.Date;

import javax.annotation.Resource;

import org.coody.framework.context.annotation.CacheWipe;
import org.coody.framework.context.annotation.CacheWrite;
import org.coody.framework.context.entity.Where;
import org.coody.framework.core.jdbc.JdbcHandle;
import org.coody.framework.util.DateUtils;
import org.springframework.stereotype.Service;

import com.ezone.web.constant.CacheFinal;
import com.ezone.web.domain.LoginLog;
import com.ezone.web.domain.UserInfo;

/**
 * @author coody
 * @date 2017年7月11日
 * @blog http://54sb.org
 * @email 644556636@qq.com
 */
@Service
public class UserService {

	@Resource
	JdbcHandle jdbcHandle;

	@CacheWrite(key = CacheFinal.USER_INFO, fields = "account", validTime = 60)
	public UserInfo loadUserInfo(String account) {
		String sql = "select * from user_info where userName=? limit 1";
		return jdbcHandle.queryFirstAuto(UserInfo.class, sql, account);
	}

	@CacheWrite(key = CacheFinal.USER_INFO, fields = "id", validTime = 72000)
	public UserInfo loadUserInfo(Integer id) {
		return jdbcHandle.findBeanFirst(UserInfo.class, "id", id);
	}
	
	@CacheWrite(key = CacheFinal.LOGIN_NEED_VERCODE, fields = "ipAddress", validTime = 3660)
	public Boolean needVerCode(String ipAddress){
		Where where=new Where()
				.set("ip", ipAddress).set("createTime", ">",DateUtils.changeDay(new Date(), -1));
		LoginLog log= jdbcHandle.findBeanFirst(LoginLog.class, where);
		if(log==null){
			return true;
		}
		return false;
	}
	
	/**
	 * 添加登录记录
	 * @param userName
	 * @param ip
	 */
	@CacheWipe(key=CacheFinal.LOGIN_NEED_VERCODE,fields="ip")
	public void writeLoginLog(String userName,String ip){
		LoginLog loginLog=new LoginLog();
		loginLog.setIp(ip);
		loginLog.setStatus(1);
		loginLog.setUserName(userName);
		jdbcHandle.insert(loginLog);
	}

}
