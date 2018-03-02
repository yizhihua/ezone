/**
 * 
 */
package com.ezone.web.domain;

import org.coody.framework.context.base.BaseModel;

/**
 * @author coody
 * @date 2017年7月11日
 * @blog http://54sb.org
 * @email 644556636@qq.com
 */
@SuppressWarnings("serial")
public class UserInfo extends BaseModel{

	private Integer id;
	private String userPwd;
	private String userName;
	private Integer roleId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	
	
	
}
