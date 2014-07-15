package com.FrameWork;

import java.io.Serializable;

/*
 * ?��?��?�� : 14.06.24 
 * ?��?��?�� : 조영�?
 * 
 */

public class Payload implements Serializable {

	private int opCode; 
	private int authCode;
	private Snapshot snapshot;
	
	// constuctor
	/**
	 * ?��버�? ?��?��?�� ?��?��?��?�� payload ?�� ?��?��?�� ?��보�?? ?��?��.
	 * @param opCode : ?��?��코드 ( 1 : ?��?��?��?�� / 2 : ?��?�� ?��?��로드 / 3 : ?��?��?���? ?���?
	 */
	public Payload(){
		
	}
	
	/**
	 * ?��버�? ?��?��?�� ?��?��?��?�� payload ?�� ?��?��?�� ?��보�?? ?��?��.
	 * @param opCode : ?��?��코드 ( 1 : ?��?��?��?�� / 2 : ?��?�� ?��?��로드 / 3 : ?��?��?���? ?���?
	 */
	public Payload(int opCode){
		this.opCode = opCode;
	}
	
	/**
	 * ?��버�? ?��?��?�� ?��?��?��?�� payload ?�� ?��?��?�� ?��보�?? ?��?��.
	 * @param opCode : ?��?��코드 ( 1 : ?��?��?��?�� / 2 : ?��?�� ?��?��로드 / 3 : ?��?��?���? ?���?
	 * @param authCode : ?��증코?�� ( mobile ?��?�� ?��?�� )
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
