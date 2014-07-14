package com.FileManager;

import java.io.File;

import com.example.timetraveler.MainActivity;

import android.os.Handler;
import android.util.Log;

public class SnapshotDiskManager {
	
	private String ssHome = null;
	private Handler uiHandler = null;
	/**
	 * 
	 * @param ssHome HomeDirectory
	 */
	public SnapshotDiskManager(String ssHome){
		this.ssHome = ssHome;
	}
	
	/**
	 * 데이터에 따라 UI 수정이 핋요할 경우 
	 * @param ssHome
	 * @param uiHandler
	 */
	public SnapshotDiskManager(String ssHome, Handler uiHandler){
		this.ssHome = ssHome;
		this.uiHandler = uiHandler;
	}
	
	
	/**
	 * 스냅샷이 존재하는 디렉토리를 날짜별로 반환한다
	 * @return
	 */
	public File[] getSnapshotList(){
		File f = new File(ssHome);
		File[] ssDirList = f.listFiles();
	
		return ssDirList;
		
	}
	
	/**
	 * 해당 날짜에 해당하는 Snapshot 파일들을 리턴한다.
	 * @param date
	 * @return
	 */
	public File[] getSnapshotFiles(String date){
		File f = new File(ssHome+date+"/");
		File[] ssList = f.listFiles();
		
		return ssList;
	}
	
	
	public File[] getSnapshotFiles(){
		File f = new File(ssHome);
		File[] ssList = f.listFiles();
		return ssList;
	}
	
	
	// Snapshot input lists
	public String[] getSnapshotInfoList(){
		String[] s = new String[100];
		
		return s; 
	}
	
	
	
}
