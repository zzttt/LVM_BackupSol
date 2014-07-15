package com.FrameWork;

import java.io.File;
import java.io.Serializable;

/*
 * ?��?��?�� : 14.06.24 
 * ?��?��?�� : 조영�?
 * 
 */

public class Snapshot implements Serializable{
	private int id;
	private int ssNumber; // Snapshot ?�� 번호
	private int ssTotal;  // Snapshot ?�� 분할 ?��축시 분할?�� 개수
	private int state; // Snapshot state
	private int date;
	private int type;
	private String path; // Snapshot?�� 존재?��?�� 경로

	public Snapshot(){
		
	}
	
	public Snapshot(int id, int ssNumber){
		this.id = id;
		this.ssNumber = ssNumber;
	}
	
	/**
	 * 
	 * @param id
	 * @param ssNumber
	 * @param state
	 * @param date
	 * @param type
	 * @param path ?��?��?��?�� 존재?��?�� ?��?��?���? 경로
	 */
	public Snapshot(int id, int ssNumber, int state, int date, int type, String path){
		this.id = id;
		this.ssNumber = ssNumber;
		this.state = state;
		this.date = date;
		this.type = type;
		this.path = path;
		
		File f = new File(path);
		this.ssTotal =  f.list().length; // ?���? snapshot 분할?��?���? 초기?��
		
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
