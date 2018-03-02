package com.ezone.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.coody.framework.context.entity.Pager;
import org.coody.framework.core.controller.BaseController;
import org.coody.framework.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ezone.web.domain.SiteMap;
import com.ezone.web.domain.ToolsInfo;
import com.ezone.web.queue.ReadNumQueue;
import com.ezone.web.schema.JournalSchema;
import com.ezone.web.service.JournalService;
import com.ezone.web.service.SiteMapService;
import com.ezone.web.service.ToolsService;

@Controller
@RequestMapping("/")
public class EzoneController extends BaseController{

	
	@Resource
	JournalService journalService;
	@Resource
	SiteMapService siteMapService;
	@Resource
	ToolsService toolsService;

	@RequestMapping(value={"index","","/"})
	public String index(HttpServletRequest req, HttpServletResponse res) {
		Integer page=getParaInteger("page");
		Pager journalPager=journalService.loadJournalPager(page, null);
		setAttribute("journalPager", journalPager);
		loadBaseData();
		return "index";
	}

	@RequestMapping(value={"admin"})
	public String admin(HttpServletRequest req, HttpServletResponse res) {
		return "redirect:admin/login."+req.getAttribute("defSuffix");
	}

	@RequestMapping(value = { "journal_{page:\\d+}_{type:\\d+}" })
	public String journal(@PathVariable Integer page,
			@PathVariable Integer type, HttpServletRequest req) {
		loadBaseData();
		Pager journalPager=journalService.loadJournalPager(page, type);
		setAttribute("journalPager", journalPager);
		setAttribute("type", type);
		return "index";
	}
	@RequestMapping(value = { "journal_{page:\\d+}" })
	public String journal(@PathVariable Integer page, HttpServletRequest req) {
		loadBaseData();
		Pager journalPager=journalService.loadJournalPager(page, null);
		setAttribute("journalPager", journalPager);
		return "index";
	}
	
	@RequestMapping(value = { "article_{id:\\d+}" })
	public String article(@PathVariable Integer id, HttpServletRequest req) {
		loadBaseData();
		JournalSchema journal=journalService.loadJournalInfo(id);
		if(!StringUtil.isNullOrEmpty(journal)){
			ReadNumQueue.readNumQueue.add(id);
		}
		setAttribute("journal", journal);
		return "article";
	}
	public void loadBaseData(){
		setAttribute("newJournals", journalService.loadNews());
		setAttribute("hotJournals", journalService.loadHots());
	}
	
	
	@RequestMapping("sitemap")
	public String sitemap() {
		SiteMap siteMap = siteMapService.loadSiteMap();
		setAttribute("siteMap", siteMap);
		loadBaseData();
		return "sitemap";
	}
	
	@RequestMapping("tools")
	public String tools(HttpServletRequest req) {
		List<ToolsInfo> tools=toolsService.loadTools();
		setAttribute("tools", tools);
		loadBaseData();
		return "tools";
	}
	@RequestMapping("robots")
	private String robots() {
		return "robots";
	}

	
}
