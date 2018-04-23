package com.ezone.web.queue;

import java.util.List;

import org.coody.framework.context.base.BaseLogger;
import org.coody.framework.util.SpringContextHelper;
import org.coody.framework.util.StringUtil;

import com.ezone.web.domain.JournalInfo;
import com.ezone.web.service.JournalService;

public class ClearMessyJournal {
	private static BaseLogger logger = BaseLogger.getLogger(ClearMessyJournal.class);

	public static void clearMessy() {
		JournalService journalService = SpringContextHelper.getBean(JournalService.class);
		Integer lastId = 0;
		while (lastId > -1) {
			List<JournalInfo> journals = journalService.getJournalList(lastId);
			if (journals == null || journals.size() == 0) {
				break;
			}
			for (JournalInfo journal : journals) {
				if (StringUtil.isMessyCode(journal.getTitle())
						||journal.getTitle().contains("锛")
						||journal.getTitle().contains("堝")
						||journal.getTitle().contains("叚")
						||journal.getTitle().contains("夋")
						||journal.getTitle().contains("暣")
						||journal.getTitle().contains("鍚")
						||journal.getTitle().contains("浜")
						||journal.getTitle().contains("戞")
						||journal.getTitle().contains("湇")
						||journal.getTitle().contains("鍔")
						||journal.getTitle().contains("灦")
						||journal.getTitle().contains("鏋")
						||journal.getTitle().contains("浼")
						||journal.getTitle().contains("佷")
						||journal.getTitle().contains("笟")
						||journal.getTitle().contains("浜")
						||journal.getTitle().contains("戞")
						||journal.getTitle().contains("灦")
						||journal.getTitle().contains("鏋")
						
						) {
					logger.error("删除文章：" + lastId);
					journalService.deleteJournal(journal.getId());
				}
				lastId = journal.getId();
			}
			logger.error("当前ID：" + lastId);
			if (journals.size() < 400) {
				lastId = -1;
			}
		}
	}

	public static void main(String[] args) {
		System.out.println(StringUtil.isMessyCode("锛堝叚锛夋暣鍚坰pring cloud浜戞湇鍔℃灦鏋? 浼佷笟浜戞灦鏋刢ommon-service浠ｇ爜缁撴瀯鍒嗘瀽"));
	}
}
