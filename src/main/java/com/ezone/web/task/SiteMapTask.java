package com.ezone.web.task;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.coody.framework.context.entity.HttpEntity;
import org.coody.framework.util.DateUtils;
import org.coody.framework.util.HttpHandle;
import org.coody.framework.util.StringUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ezone.web.domain.JournalInfo;
import com.ezone.web.domain.SiteMap;
import com.ezone.web.service.JournalService;
import com.ezone.web.service.SiteMapService;

@Component
public class SiteMapTask {

	@Resource
	SiteMapService siteMapService;
	@Resource
	JournalService journalService;

	@Scheduled(cron="0 0 0,6,12,18 * * ?")
	public void parsSiteMap() {
		try {
			SiteMap siteMap = siteMapService.loadSiteMapNoCache();
			if(siteMap==null){
				siteMap=new SiteMap();
			}
			siteMap.setLastId(siteMap.getLastId() == null ? 0 : siteMap.getLastId());
			List<JournalInfo> list = journalService.getJournalList(siteMap.getLastId());
			if (StringUtil.isNullOrEmpty(list)) {
				siteMap.setLastId(0);
				siteMapService.saveSiteMap(siteMap);
				return;
			}
			StringBuffer sbXml = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			StringBuffer sbHtml = new StringBuffer("");
			StringBuilder sbPost = new StringBuilder("");
			sbXml.append("<urlset>");
			for (JournalInfo tmp : list) {
				if (tmp != null && tmp.getId() > 0) {
					String url = MessageFormat.format("http://{0}/article_{1}.html", siteMap.getDomain(),
							String.valueOf(tmp.getId()));
					sbXml.append("\r\n").append("<url>");
					sbXml.append("\r\n").append("<loc>").append(url).append("</loc>");
					sbXml.append("\r\n").append("<priority>1.0</priority>");
					sbXml.append("\r\n").append("<lastmod>")
							.append(DateUtils.toString(new Date(),"yyyy-MM-dd"))
							.append("</lastmod>");
					sbXml.append("\r\n<changefreq>daily</changefreq>");
					sbXml.append("\r\n").append("</url>");
					sbHtml.append("<li>");
					sbHtml.append("<a href=\"");
					sbHtml.append(url);
					sbHtml.append("\" title=\"");
					sbHtml.append(tmp.getTitle());
					sbHtml.append("\" target=\"_blank\">");
					sbHtml.append(tmp.getTitle());
					sbHtml.append("</a></li>");
					sbPost.append("http://").append(url).append("\r\n");
				}
				siteMap.setLastId(tmp.getId());
			}
			sbXml.append("\r\n").append("</urlset>");
			siteMap.setUpdateTime(new Date());
			siteMap.setHtml(sbHtml.toString());
			siteMap.setXml(sbXml.toString());
			if (list.size() < 400) {
				siteMap.setLastId(0);
			}
			siteMapService.saveSiteMap(siteMap);
			postBaidu(siteMap.getBaiduUrl(), sbPost.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void postBaidu(String url, String postData) {
		try {
			HttpHandle http=new HttpHandle();
			 http.Post(url, "http://www.54sb.org/sitemap.xml");
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			HttpHandle http=new HttpHandle();
			HttpEntity entity=http.Post(url, postData);
			System.out.println(entity.getHtml());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
