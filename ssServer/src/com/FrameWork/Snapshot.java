package com.FrameWork;

import java.io.File;
import java.io.Serializable;

/*
 * 작성일 : 14.06.24 
 * 작성자 : 조영민
 * 
 */

public class Snapshot implements Serializable{
	private int id;
	private int ssNumber; // Snapshot 의 번호
	private int ssTotal;  // Snapshot 이 분할 압축시 분할된 개수
	private int state; // Snapshot state
	private int date;
	private int type;
	private String path; // Snapshot이 존재하는 경로

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
	 * @param date 스냅샷 생성 날짜
	 * @param type 
	 * @param path 스냅샷이 존재하는 디렉토리 경로
	 */
	public Snapshot(int id, int ssNumber, int state, int date, int type, String path){
		this.id = id;
		this.ssNumber = ssNumber;
		this.state = state;
		this.date = date;
		this.type = type;
		this.path = path;
		
		File f = new File(path);
		this.ssTotal =  f.list().length; // 전체 snapshot 분할사이즈 초기화
		
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
