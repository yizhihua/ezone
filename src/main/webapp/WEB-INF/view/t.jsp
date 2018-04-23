<%@page import="org.coody.framework.util.StringUtil"%>
<%@page import="com.ezone.web.domain.JournalInfo"%>
<%@page import="org.coody.framework.util.SpringContextHelper"%>
<%@page import="com.ezone.web.service.JournalService"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	JournalService journalService=SpringContextHelper.getBean(JournalService.class);
	Integer lastId=0;
	while(lastId>0){
		List<JournalInfo> journals=journalService.getJournalList(0);
		if(journals==null||journals.size()==0){
			break;
		}
		for(JournalInfo journal:journals){
			if(StringUtil.isMessyCode(journal.getTitle())){
				journalService.deleteJournal(journal.getId());
			}
			lastId=journal.getId();
		}
		if(journals.size()<400){
			lastId=-1;
		}
	}
	
 %>
