package com.ezone.web.queue;

import java.util.List;

import org.coody.framework.context.base.BaseLogger;
import org.coody.framework.util.SpringContextHelper;
import org.coody.framework.util.StringUtil;

import com.ezone.web.domain.JournalInfo;
import com.ezone.web.service.JournalService;

public class ClearMessyJournal {
	private static BaseLogger logger=BaseLogger.getLogger(ClearMessyJournal.class);
	
	public static void clearMessy() {
		JournalService journalService=SpringContextHelper.getBean(JournalService.class);
		Integer lastId=0;
		while(lastId>0){
			List<JournalInfo> journals=journalService.getJournalList(0);
			if(journals==null||journals.size()==0){
				break;
			}
			for(JournalInfo journal:journals){
				if(StringUtil.isMessyCode(journal.getTitle())){
					logger.error("删除文章："+lastId);
					journalService.deleteJournal(journal.getId());
				}
				lastId=journal.getId();
			}
			logger.error("当前ID："+lastId);
			if(journals.size()<400){
				lastId=-1;
			}
		}
	}
}
