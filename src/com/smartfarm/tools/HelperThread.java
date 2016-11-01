package com.smartfarm.tools;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.os.Handler;

public class HelperThread {

	private volatile boolean threadIsWork = false;
	private boolean running = true;
	private static HelperThread mThread;
	private BlockingQueue<AssistThreadWork> otherworks; 
	private Handler mHandler = new Handler();

	public static HelperThread getInstance() {
		
		if(mThread == null)
			mThread = new HelperThread();
		
		return mThread;
	}
	
	private HelperThread() {
		otherworks = new LinkedBlockingQueue<AssistThreadWork>();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(running) {
					
					while(!otherworks.isEmpty()) {
						try {
							AssistThreadWork work = otherworks.take();
							work.working();
							
							publish(work);
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					setThreadStop();
					waitForUiThread();
				}
			}
		}).start();
	}
	
	public void publish(final AssistThreadWork work) {
		
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				
				work.ok();
			}
		});
	}
	
	public synchronized void setThreadWork(AssistThreadWork work) {
		
		otherworks.add(work);
		
		if (!threadIsWork) {
			threadIsWork = true;
			this.notify();
		}
	}
	
	private synchronized void setThreadStop() {
		threadIsWork = false;
	}
	
	private synchronized void waitForUiThread() {
		while(!threadIsWork) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}
	
	public void setStopWork() {
		running = false;
	}
}
