package com.FrameWork;

import java.io.Serializable;

/*
 * ?‘?„±?¼ : 14.06.24 
 * ?‘?„±? : ì¡°ì˜ë¯?
 * 
 */

public class Payload implements Serializable {

	private int opCode; 
	private int authCode;
	private Snapshot snapshot;
	
	// constuctor
	/**
	 * ?„œë²„ì? ?†µ?‹ ?— ?´?š©?˜?Š” payload ?— ?•„?š”?•œ ? •ë³´ë?? ?‹´?Œ.
	 * @param opCode : ?™?‘ì½”ë“œ ( 1 : ?ŒŒ?¼? „?†¡ / 2 : ?ŒŒ?¼ ?‹¤?š´ë¡œë“œ / 3 : ?ŒŒ?¼? •ë³? ?½ê¸?
	 */
	public Payload(){
		
	}
	
	/**
	 * ?„œë²„ì? ?†µ?‹ ?— ?´?š©?˜?Š” payload ?— ?•„?š”?•œ ? •ë³´ë?? ?‹´?Œ.
	 * @param opCode : ?™?‘ì½”ë“œ ( 1 : ?ŒŒ?¼? „?†¡ / 2 : ?ŒŒ?¼ ?‹¤?š´ë¡œë“œ / 3 : ?ŒŒ?¼? •ë³? ?½ê¸?
	 */
	public Payload(int opCode){
		this.opCode = opCode;
	}
	
	/**
	 * ?„œë²„ì? ?†µ?‹ ?— ?´?š©?˜?Š” payload ?— ?•„?š”?•œ ? •ë³´ë?? ?‹´?Œ.
	 * @param opCode : ?™?‘ì½”ë“œ ( 1 : ?ŒŒ?¼? „?†¡ / 2 : ?ŒŒ?¼ ?‹¤?š´ë¡œë“œ / 3 : ?ŒŒ?¼? •ë³? ?½ê¸?
	 * @param authCode : ?¸ì¦ì½”?“œ ( mobile ?—?„œ ?ƒ?„± )
	 */
	public Payload(int opCode, int authCode){
		this.opCode = opCode;
		this.authCode = authCode;
	}
	
	public int getOpCode(){
		return this.opCode;
	}
	
	public int getAuth(){
		return this.authCode;
	}
	
	public void setOpCode(int opCode){
		this.opCode = opCode;
	}
	
	public void setAuth(int authCode){
		this.authCode = authCode;
	}
	
	public void setSnapshot(Snapshot snapshot){
		this.snapshot = snapshot;
	}
	
}
