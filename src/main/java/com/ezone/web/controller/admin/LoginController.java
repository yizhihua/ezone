package com.ezone.web.controller.admin;

import java.io.IOException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.coody.framework.context.entity.MsgEntity;
import org.coody.framework.core.cache.LocalCache;
import org.coody.framework.core.controller.BaseController;
import org.coody.framework.util.EncryptUtil;
import org.coody.framework.util.PrintException;
import org.coody.framework.util.RequestUtil;
import org.coody.framework.util.StringUtil;
import org.coody.framework.util.VerificationCodeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezone.web.constant.CacheFinal;
import com.ezone.web.domain.UserInfo;
import com.ezone.web.service.MenuService;
import com.ezone.web.service.UserService;

@Controller
@RequestMapping("/admin")
public class LoginController extends BaseController{

	@Resource
	UserService userService;
	@Resource
	MenuService menuService;

	
	@RequestMapping({"/login","","/"})
	public String login(HttpServletResponse res) {
		Boolean needVerCode=userService.needVerCode(RequestUtil.getIpAddr(request));
		setAttribute("needVerCode", needVerCode);
		return "admin/login";
	}

	@RequestMapping("/index")
	public String index() {
		return "redirect:simple/index."+getSessionPara("defSuffix");
	}
	@RequestMapping("/loginOut")
	public String loginOut() {
		RequestUtil.setUser(request, null);
		return "redirect:" + request.getScheme() + ":" + getSessionPara("basePath").toString() + "admin/login."
				+ getSessionPara("defSuffix").toString();
	}
	@RequestMapping("/doLogin")
	@ResponseBody
	public Object doLogin(String userName, String userPwd) {
		try {
			RequestUtil.setUser(request, null);
			if (StringUtil.hasNull(userName, userPwd)) {
				return new MsgEntity(-1, "参数有误");
			}
			Boolean needVerCode=userService.needVerCode(RequestUtil.getIpAddr(request));
			String verCode=getPara("verCode");
			if(needVerCode){
				if(StringUtil.isNullOrEmpty(verCode)){
					return new MsgEntity(-1, "参数有误");
				}
				String sysCode=RequestUtil.getCode(request);
				if(!verCode.equals(sysCode)){
					return new MsgEntity(-1, "验证码有误");
				}
			}
			String loginKey = CacheFinal.LOGIN_NUM + userName;
			Integer num = LocalCache.getCache(loginKey);
			if (num == null) {
				num = 0;
			}
			if (num != null && num > 10) {
				LocalCache.setCache(loginKey, num + 1, 60);
				return new MsgEntity(-1, "登录过于频繁");
			}
			LocalCache.setCache(loginKey, num + 1);
			UserInfo userInfo = userService.loadUserInfo(userName);
			if (userInfo == null) {
				return new MsgEntity(-1, "用户不存在");
			}
			if (!userInfo.getUserPwd().equals(EncryptUtil.customEnCode(userPwd))) {
				return new MsgEntity(-1, "密码不正确");
			}
			LocalCache.delCache(loginKey);
			RequestUtil.setUser(request, userInfo);
			setSessionPara("menus", menuService.loadMenus(userInfo.getRoleId()));
			return new MsgEntity(0, "登录成功");
		} catch (Exception e) {
			PrintException.printException(logger, e);
			return new MsgEntity(-1, "系统繁忙");
		}finally{
			if(!StringUtil.isNullOrEmpty(RequestUtil.getUser(request))){
				userService.writeLoginLog(userName, RequestUtil.getIpAddr(request));
			}
		}
	}
	
	@RequestMapping(value = "/verCode")
	public void verCode(HttpServletResponse response) throws ServletException, IOException {
		String verCode = VerificationCodeUtil.getCodeStr(4);
		RequestUtil.setCode(request, verCode);
		ServletOutputStream out = response.getOutputStream();
		response.setContentType("image/gif");
		ImageIO.write(VerificationCodeUtil.outCode(120, 42, 4, 24, verCode), "png", out);
	}
}
