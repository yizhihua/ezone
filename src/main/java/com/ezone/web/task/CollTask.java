package com.ezone.web.task;

import java.lang.reflect.Modifier;

import javax.annotation.Resource;

import org.coody.framework.context.annotation.LogHead;
import org.coody.framework.context.base.BaseLogger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ezone.web.coll.CollJournalInfo;
import com.ezone.web.coll.CollJournalType;
import com.ezone.web.util.BaiduHttpUtil;

@Component
public class CollTask {

	BaseLogger logger = BaseLogger.getLogger(CollTask.class);

	private static boolean isColled = false;
	
	
	
	@Resource
	CollJournalType collJournalType;
	@Resource
	CollJournalInfo collJournalInfo;

	@Scheduled(cron = "0 0 0 * * ? ")
	@LogHead("刷新文章类别任务")
	public void refreshType() {
		collJournalType.collTypeInfos();
	}
	

	@Scheduled(cron = "0 0/10 * * * ? ")
	@LogHead("刷新采集Cookie")
	public void refreshCollCookie() {
		BaiduHttpUtil.initCookie();
	}
	@Scheduled(cron = "0 0/1 * * * ? ")
	@LogHead("采集文章内容")
	public void collJournalInfo() {
		if (isColled) {
			logger.debug("文章正在采集，本次任务放弃");
			return;
		}
		try {
			isColled = true;
			collJournalInfo.loadJournal();
		} catch (Exception e) {
		} finally {
			isColled = false;
		}
	}
	
	public static void main(String[] args) {
			System.out.println(Modifier.FINAL);
		
	}

}
