package com.ezone.web.task;

import org.coody.framework.util.StringUtil;
import org.coody.framework.util.UploadUtil;
import org.springframework.stereotype.Component;

import com.ezone.web.queue.ImageQueue;

@Component
public class ImgDownTask {
	
	private static final String baseUrl="http://www.myexception.cn/";

	
	//@Scheduled(cron="0/1 * * * * ? ")
	public void down(){
		String url=ImageQueue.imageQueue.poll();
		while(!StringUtil.isNullOrEmpty(url)){
			try {
				UploadUtil.doDown(baseUrl+url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			url=ImageQueue.imageQueue.poll();
		}
	}
}
