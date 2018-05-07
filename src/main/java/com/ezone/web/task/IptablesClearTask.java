package com.ezone.web.task;

import org.coody.framework.context.annotation.LogHead;
import org.coody.framework.context.base.BaseLogger;
import org.coody.framework.util.PrintException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class IptablesClearTask {

	BaseLogger logger = BaseLogger.getLogger(IptablesClearTask.class);


	@Scheduled(cron = "0 0 0 * * ? ")
	@LogHead("刷新文章类别任务")
	public synchronized void refreshType() {
		try {
			String shell="iptables -F ";
			Runtime.getRuntime().exec(shell);
			shell="service iptables save";
			Runtime.getRuntime().exec(shell);
			Thread.sleep(20);
		} catch (Exception e) {
			PrintException.printException(logger, e);
		}
	}
	

}