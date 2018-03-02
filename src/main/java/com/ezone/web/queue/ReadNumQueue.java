package com.ezone.web.queue;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.Resource;
import org.coody.framework.context.base.BaseLogger;
import org.coody.framework.util.PrintException;
import org.coody.framework.util.StringUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.ezone.web.service.JournalService;

@Component
public class ReadNumQueue {
	
	private static BaseLogger logger=BaseLogger.getLogger(ReadNumQueue.class);
	public static final ConcurrentLinkedQueue<Integer> readNumQueue=new ConcurrentLinkedQueue<Integer>();
	
	@Resource
	JournalService journalService;
	
	@Scheduled(cron="0/1 * * * * ? ")
	public synchronized void writeWall() throws InterruptedException{
		Integer id=readNumQueue.poll();
		while(!StringUtil.isNullOrEmpty(id)){
			try {
				journalService.addViews(id, 1);
				Thread.sleep(20);
			} catch (Exception e) {
				PrintException.printException(logger, e);
			}finally {
				id=readNumQueue.poll();
			}
		}
		
	}
}
