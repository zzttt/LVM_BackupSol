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
	 * �����Ϳ� ���� UI ������ ������ ��� 
	 * @param ssHome
	 * @param uiHandler
	 */
	public SnapshotDiskManager(String ssHome, Handler uiHandler){
		this.ssHome = ssHome;
		this.uiHandler = uiHandler;
	}
	
	
	/**
	 * �������� �����ϴ� ���丮�� ��¥���� ��ȯ�Ѵ�
	 * @return
	 */
	public File[] getSnapshotList(){
		File f = new File(ssHome);
		File[] ssDirList = f.listFiles();
	
		return ssDirList;
		
	}
	
	/**
	 * �ش� ��¥�� �ش��ϴ� Snapshot ���ϵ��� �����Ѵ�.
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