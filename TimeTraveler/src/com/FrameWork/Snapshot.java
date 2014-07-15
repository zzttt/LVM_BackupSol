package com.FrameWork;

import java.io.File;
import java.io.Serializable;

/*
 * ?‘?„±?¼ : 14.06.24 
 * ?‘?„±? : ì¡°ì˜ë¯?
 * 
 */

public class Snapshot implements Serializable{
	private int id;
	private int ssNumber; // Snapshot ?˜ ë²ˆí˜¸
	private int ssTotal;  // Snapshot ?´ ë¶„í•  ?••ì¶•ì‹œ ë¶„í• ?œ ê°œìˆ˜
	private int state; // Snapshot state
	private int date;
	private int type;
	private String path; // Snapshot?´ ì¡´ì¬?•˜?Š” ê²½ë¡œ

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
	 * @param path ?Š¤?ƒ…?ƒ·?´ ì¡´ì¬?•˜?Š” ?””? ‰?† ë¦? ê²½ë¡œ
	 */
	public Snapshot(int id, int ssNumber, int state, int date, int type, String path){
		this.id = id;
		this.ssNumber = ssNumber;
		this.state = state;
		this.date = date;
		this.type = type;
		this.path = path;
		
		File f = new File(path);
		this.ssTotal =  f.list().length; // ? „ì²? snapshot ë¶„í• ?‚¬?´ì¦? ì´ˆê¸°?™”
		
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
