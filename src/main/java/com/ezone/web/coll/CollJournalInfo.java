package com.ezone.web.coll;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.coody.framework.context.base.BaseLogger;
import org.coody.framework.context.entity.HttpEntity;
import org.coody.framework.context.entity.Pager;
import org.coody.framework.core.thread.ThreadBlockHandle;
import org.coody.framework.util.GZIPUtils;
import org.coody.framework.util.HttpHandle;
import org.coody.framework.util.PropertUtil;
import org.coody.framework.util.StringUtil;
import org.coody.framework.util.UploadUtil;
import org.springframework.stereotype.Component;

import com.ezone.web.domain.CollConfig;
import com.ezone.web.domain.JournalInfo;
import com.ezone.web.domain.TypeInfo;
import com.ezone.web.service.CollService;
import com.ezone.web.service.JournalService;
import com.ezone.web.service.TypeService;

@Component
public class CollJournalInfo{

	
	
	private final BaseLogger logger = BaseLogger.getLoggerPro(this.getClass());
	private final static String articlePattern = "<a href=\"{url}\\d{1,15}.html\" target=\"_blank\">.*?</a>";
	public static HttpHandle httpUtil=new HttpHandle();
	private final String contextPattern = "<div class=\"c_txt\">.*?</div>(?=(<div class=))";
	private final String imgPattern = "<img.*?src=\"([^\"]*)\".*?>";
	private final String baseUrl = "http://www.myexception.cn";
	
	
	@Resource
	CollService collService;
	@Resource
	TypeService typeService;
	@Resource
	JournalService journalService;

	// 格式化分页URL
	private String getPagerUrl(String url, Integer page) {
		if (page == 1) {
			return url + "/index.html";
		}
		return url + "/index_" + String.valueOf(page) + ".html";
	}

	// 加载页码文章列表
	private Pager loadPager(TypeInfo type, Pager pager) {
		String url = getPagerUrl(type.getOtherUrl(), pager.getCurrPage());
		logger.info(type.getTypeName() + ",[页码:" + pager.getCurrPage() + "]抓取页面文章列表============");
		JournalVO vo = null;
		List<JournalVO> vos = new ArrayList<JournalVO>();
		try {
			HttpEntity hEntity = httpUtil.Get(url);
			if (hEntity == null || hEntity.getCode() != 200) {
				logger.debug(type.getTypeName() + ",页面内容为空============");
				return null;
			}
			String html = hEntity.getHtml();
			String pattern = articlePattern.replace("{url}", type.getOtherUrl());
			List<String> artices = StringUtil.doMatcher(html, pattern);
			if (StringUtil.isNullOrEmpty(artices)) {
				logger.debug("无任何文章:" + url);
				return null;
			}
			for (String artice : artices) {
				if (StringUtil.isNullOrEmpty(artice)) {
					continue;
				}
				vo = new JournalVO();
				vo.setTitle(StringUtil.textCutCenter(artice, ">", "<"));
				vo.setUrl(StringUtil.textCutCenter(artice, "href=\"", "\""));
				vos.add(vo);
			}
			// 加载总页数
			if (StringUtil.isNullOrEmpty(pager.getTotalPage()) || pager.getTotalPage() == 1) {
				html = StringUtil.textCutCenter(html, "<div class=\"c_p_s\">", "</div>");
				String[] pageA = html.split("</li>");
				for (int i = 0; i < pageA.length; i++) {
					String page = StringUtil.textCutCenter(pageA[i], "href=\"index_", ".html");
					if (StringUtil.isNumber(page)) {
						pager.setTotalPage(StringUtil.toInteger(page));
					}
				}
			}
			logger.debug(type.getTypeName() + ",文章列表获取到:" + pager.getTotalPage() + "页============");
			pager.setData(vos);
			return pager;
		} catch (Exception e) {
			return null;
		}
	}

	public void loadJournal() {
		logger.debug("加载文章类别列表============");
		List<TypeInfo> types = typeService.getTypes();
		CollConfig config=collService.loadCollConfig();
		if (StringUtil.isNullOrEmpty(types)) {
			logger.debug("文章类别列表为空,放弃本次采集============");
			return;
		}
		types=PropertUtil.doSeq(types, "id");
		for(TypeInfo type:types){
			if(config!=null){
				if(type.getId()<config.getTypeId().intValue()){
					continue;
				}
			}
			Pager pager = new Pager();
			if(config!=null){
				if(config.getTypeId()!=type.getId().intValue()){
					config.setCollPage(0);
					config.setTotalPage(0);
				}
				pager.setCurrPage(config.getCollPage());
			}
			try {
				pager = loadPager(type, pager);
				loadJournalInfo(pager, type);
				while (pager.getCurrPage() < pager.getTotalPage()) {
					try {
						Thread.sleep(50);
						logger.info("采集文章:"+type.getTypeName());
						pager.setCurrPage(pager.getCurrPage() + 1);
						pager = loadPager(type, pager);
						loadJournalInfo(pager, type);
						if (pager.getCurrPage() % 10 == 0) {
							Thread.sleep(1000);
						}
					} catch (Exception e) {
					}finally {
						config=new CollConfig();
						config.setTypeId(type.getId());
						config.setCollPage(pager.getCurrPage());
						config.setTotalPage(pager.getTotalPage());
						if(type.getId()==types.get(types.size()-1).getId().intValue()){
							collService.removeCollConfig();
							return;
						}
						collService.writeCollConfig(config);
					}
				}
			} catch (Exception e) {
			}
		}
	}

	// 加载文章列表内容
	private void loadJournalInfo(Pager pager, TypeInfo types) {
		logger.debug(types.getTypeName() + ",获取文章内容=============");
		if (StringUtil.isNullOrEmpty(pager)) {
			pager = new Pager();
			logger.debug(types.getTypeName() + ",页面为空=============");
			pager.setCurrPage(1);
			pager.setTotalPage(1);
			return;
		}
		if (StringUtil.isNullOrEmpty(pager.getData())) {
			logger.debug(types.getTypeName() + ",页面无数据=============");
			pager = new Pager();
			pager.setCurrPage(1);
			pager.setTotalPage(1);
			return;
		}
		List<JournalVO> vos = pager.getData();
		ThreadBlockHandle blockHandle=new ThreadBlockHandle(20,120);
		for (JournalVO vo : vos) {
			try {
				JournalInfo journal=journalService.loadJournalInfo(vo.getTitle());
				if(!StringUtil.isNullOrEmpty(journal)){
					logger.info("文章已存在:"+vo.getTitle());
					continue;
				}
				if(StringUtil.isMessyCode(vo.getTitle())){
					logger.info("文章乱码:"+vo.getTitle());
					continue;
				}
				blockHandle.pushTask(new Runnable() {
					@Override
					public void run() {
						try {
							loadJournalContext(vo, types);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				});
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		blockHandle.execute();
	}

	// 加载单篇文章内容
	private void loadJournalContext(JournalVO vo, TypeInfo type) throws UnsupportedEncodingException {
		if (StringUtil.isNullOrEmpty(vo.getTitle())) {
			logger.debug(type.getTypeName() + "," + vo.getTitle() + ",文章标题为空:");
			return;
		}
		logger.debug(type.getTypeName() + "," + vo.getTitle() + ",获得文章页面信息=============");
		Map<String, String> imgMap = new HashMap<String, String>();
		String context = getJournalContext(vo.getUrl(), imgMap);
		if (StringUtil.isNullOrEmpty(context)) {
			logger.debug(type.getTypeName() + "," + vo.getTitle() + ",文章内容为空=============");
			return;
		}
		JournalInfo journal=new JournalInfo();
		journal.setTypeId(type.getId());
		journal.setTitle(vo.getTitle());
		journal.setContext(GZIPUtils.compress(context.getBytes("UTF-8")));
		journal.setViews(StringUtil.getRanDom(400, 2200));
		journal.setTime(new Date());
		try {
			Long code = journalService.writeJournal(journal);
			if (code < 1) {
				delDownImg(imgMap);
				return;
			}
			logger.info("文章入库成功:"+journal.getTitle());
		} catch (Exception e) {
			logger.debug(type.getTypeName() + "," + vo.getTitle() + ",文章已存在=============");
			delDownImg(imgMap);
			return;
		}
	}

	private String getJournalContext(String url, Map<String, String> map) {
		HttpEntity hEntity = httpUtil.Get(url);
		if (hEntity == null || StringUtil.isNullOrEmpty(hEntity.getHtml())) {
			return null;
		}
		String html = hEntity.getHtml();
		try {
			Thread.sleep(10);
			String context = StringUtil.doMatcherFirst(html, contextPattern);
			if (StringUtil.isNullOrEmpty(context)) {
				logger.debug("文章内容为空:" + url);
				return null;
			}
			
			if(context.getBytes("UTF-8").length>262144){
				logger.error("文章内容过长:"+url);
				return null;
			}
			if (context.length() < 2500) {
				logger.debug("文章内容过短:" + url);
				return null;
			}
			// 加载图片列表
			List<String> images = getImages(context, imgPattern);
			if (StringUtil.isNullOrEmpty(images)) {
				return context;
			}
			// 下载图片
			context = parsImage(context, images, map);
			return context;
		} catch (Exception e) {
			delDownImg(map);
		}
		return null;
	}

	private String parsImage(String context, List<String> imageUrls, Map<String, String> map) throws Exception {
		for (String imageUrl : imageUrls) {
			try {
				String imgPath = map.get(imageUrl);
				if (!StringUtil.isNullOrEmpty(imgPath)) {
					context = context.replace(imageUrl, imgPath);
					continue;
				}
				// 下载图片
				imgPath = downImg(baseUrl, imageUrl);
				Thread.sleep(20);
				logger.info("下载图片:" + imageUrl);
				// 替换超链接图片
				if (!StringUtil.isNullOrEmpty(imgPath)) {
					map.put(imageUrl, imgPath);
					context = context.replace(imageUrl, imgPath);
					continue;
				}
				// 删除已下载图片
				delDownImg(map);
				throw new Exception("图片下载失败");
			} catch (Exception e) {
				logger.error(baseUrl + imageUrl + "图片下载失败!");
				// 删除已下载图片
				delDownImg(map);
				throw new Exception("图片下载失败");
			}
		}
		return context;
	}
	
	private void delDownImg(Map<String, String> fileMap) {
		if (StringUtil.isNullOrEmpty(fileMap)) {
			return;
		}
		String basePath = System.getProperty("ezone.root");
		for (String key : fileMap.keySet()) {
			String imgPath = basePath + fileMap.get(key);
			try {
				logger.info("删除图片:" + imgPath);
				new File(imgPath).delete();
			} catch (Exception e) {
			}
		}
	}

	private List<String> getImages(String context, String pat) {
		try {
			Set<String> images = new HashSet<String>();
			Integer index = 0;
			Pattern pattern = Pattern.compile(pat, Pattern.DOTALL);
			Matcher matcher = pattern.matcher(context);
			String tmp = null;
			while (matcher.find(index)) {
				tmp = matcher.group(1);
				index = matcher.end();
				if (StringUtil.isNullOrEmpty(tmp)) {
					continue;
				}
				images.add(tmp);
				Thread.sleep(1);
			}
			return new ArrayList<String>(images);
		} catch (Exception e) {
			
			return null;
		}
	}

	private String downImg(String baseUrl, String imgUri) {
		try {
			return UploadUtil.doDown(baseUrl + imgUri);
		} catch (Exception e) {
			
			return null;
		}
	}

	private static class JournalVO {

		private String title;
		private String url;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}


}
