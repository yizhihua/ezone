package org.coody.framework.core.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SysThreadHandle {
	public static final int CORESIZE_NORMAL=20;
	public static final int MAXCORESIZE = 200;
	public static final int KEEPALIVETIME = 10;  //10s
	public static final ExecutorService  sysThreadPool =  new ThreadPoolExecutor(CORESIZE_NORMAL,MAXCORESIZE,
	          KEEPALIVETIME,TimeUnit.SECONDS,
	          new LinkedBlockingQueue<Runnable>()); 
}
