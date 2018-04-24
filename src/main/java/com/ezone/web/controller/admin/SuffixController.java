package com.ezone.web.controller.admin;

import java.util.List;

import javax.annotation.Resource;

import org.coody.framework.context.annotation.Power;
import org.coody.framework.context.entity.MsgEntity;
import org.coody.framework.context.entity.Pager;
import org.coody.framework.core.controller.BaseController;
import org.coody.framework.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezone.web.domain.SuffixInfo;
import com.ezone.web.domain.SuffixStatic;
import com.ezone.web.service.SuffixService;

@RequestMapping("/admin/suffix")
@Controller
public class SuffixController extends BaseController{

	@Resource
	SuffixService suffixService;
	
	@RequestMapping("/suffixManage")
	@Power("suffixManage")
	public String suffixManage(){
		Pager pager=getBeanAll(Pager.class);
		pager=suffixService.loadSuffixs(pager, getPara("keyWorld"));
		setAttribute("dataPager", pager);
		keepParas();
		return "admin/suffix/suffix_list";
	}
	
	@RequestMapping("/openSuffix")
	@Power("suffixManage")
	@ResponseBody
	public Object openSuffix(){
		String suffix=getPara("suffix");
		Integer status=getParaInteger("status");
		Long code=suffixService.updateStatus(suffix, status);
		if(code<1){
			return new MsgEntity(-1,"操作失败");
		}
		removeSessionPara("defSuffix");
		return new MsgEntity(0,"操作成功");
	}
	
	@RequestMapping("/delSuffix")
	@Power("suffixManage")
	@ResponseBody
	public Object delSuffix(){
		String suffix=getPara("suffix");
		SuffixInfo suffixInfo=suffixService.loadSuffix(suffix);
		if(suffixInfo==null){
			return new MsgEntity(-1,"后缀不存在");
		}
		if(suffixInfo.getStatus()==2){
			return new MsgEntity(-1,"默认后缀不可删除");
		}
		Long code=suffixService.delSuffix(suffix);
		if(code<1){
			return new MsgEntity(-1,"操作失败");
		}
		return new MsgEntity(0,"操作成功");
	}
	
	@RequestMapping("/addSuffix")
	@Power("suffixManage")
	public String addSuffix(){
		return "admin/suffix/suffix_edit";
	}
	
	@RequestMapping("/saveSuffix")
	@Power("suffixManage")
	@ResponseBody
	public Object saveSuffix(){
		SuffixInfo suffixInfo=getBeanAll(SuffixInfo.class);
		if(suffixInfo==null||StringUtil.isNullOrEmpty(suffixInfo.getSuffix())){
			return new MsgEntity(-1,"参数有误");
		}
		SuffixInfo suffixInfoInDb=suffixService.loadSuffix(suffixInfo.getSuffix());
		if(suffixInfoInDb!=null){
			return new MsgEntity(-1,"后缀已存在");
		}
		List<String> staticSuffixs=suffixService.loadStaSuffix();
		if(staticSuffixs!=null&&staticSuffixs.contains(suffixInfo.getSuffix())){
			return new MsgEntity(-1,"不能添加该后缀");
		}
		Long code=suffixService.addSuffix(suffixInfo);
		if(code<1){
			return new MsgEntity(-1,"操作失败");
		}
		return new MsgEntity(0,"操作成功");
	}
}
