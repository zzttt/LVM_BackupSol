package com.FrameWork;

import java.io.File;
import java.io.Serializable;

/*
 * �ۼ��� : 14.06.24 
 * �ۼ��� : ������
 * 
 */

public class Snapshot implements Serializable{
	private int id;
	private int ssNumber; // Snapshot �� ��ȣ
	private int ssTotal;  // Snapshot �� ���� ����� ���ҵ� ����
	private int state; // Snapshot state
	private int date;
	private int type;
	private String path; // Snapshot�� �����ϴ� ���

	public Snapshot(){
		
	}
	
	/**
	 * 
	 * @param id snapshot ID(user ID)
	 * @param ssNumber 
	 */
	public Snapshot(int id, int ssNumber){
		this.id = id;
		this.ssNumber = ssNumber;
	}
	
	/**
	 * 
	 * @param id
	 * @param ssNumber
	 * @param state 
	 * @param date ������ ���� ��¥
	 * @param type 
	 * @param path �������� �����ϴ� ���丮 ���
	 */
	public Snapshot(int id, int ssNumber, int state, int date, int type, String path){
		this.id = id;
		this.ssNumber = ssNumber;
		this.state = state;
		this.date = date;
		this.type = type;
		this.path = path;
		
		File f = new File(path);
		this.ssTotal =  f.list().length; // ��ü snapshot ���һ����� �ʱ�ȭ
		
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setSnapshotNum(int ssNumber){
		this.ssNumber = ssNumber;
	}
	
	public void setState(int state){
		this.state = state;
	}
	
	public void setDate(int date){
		this.date = date;
	}
	
	public void setType(int type){
		this.type = type;
	}
	
	public void setPath(String path){
		this.path = path;
	}
	
	public String SnapshotInfo(){
		String result = null;
		result = "sId = "+this.id+"\n sNumber = "+this.ssNumber+"\n state = "+this.state;
		return result;
	}
			
}		
