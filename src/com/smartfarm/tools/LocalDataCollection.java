package com.smartfarm.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import android.os.Environment;

public class LocalDataCollection {
	private static LocalDataCollection collector;
	private File root;
	private File record;
	
	public static LocalDataCollection getInstance() {
		
		if(collector == null)
			collector = new LocalDataCollection();
		
		return collector;
	}

	private LocalDataCollection() {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		
		File sdcardPath = null;
		if (sdCardExist) {
			sdcardPath = Environment.getExternalStorageDirectory();// 获取跟目录
			
			String rootPath = sdcardPath.getAbsolutePath() + Constants.LOCAL_DATA_ROOT_PATH;
			root = new File(rootPath);
			
			record = new File(rootPath + "record" + ".txt");
			
		} else {
			ToastTool.showToast("不存在sd卡");
		}
	}
	
	private boolean check() {
		if(root == null)
			return false;
		
		if (!root.exists()) 
			root.mkdirs();
		
		try {
			if(!record.exists())
				record.createNewFile();
			
			return true;
			
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean addRecord(String record) {
		check();
		
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(this.record, true));
			
			out.append(record + "\r\n");
			out.flush();
			
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if(out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean addErrorLog(String errorlog) {
		check();
		
		File errorLog = new File(root.getAbsoluteFile() + File.separator + "error" + CommonTool.getDateString2(new Date()) + ".txt");
		
		try {
			errorLog.createNewFile();
		} catch(Exception e) {
			return false;
		}
		
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(errorLog, true));
			
			out.append(errorlog);
			out.flush();
			
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if(out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
