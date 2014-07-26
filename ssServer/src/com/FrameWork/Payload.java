package com.FrameWork;

import java.io.Serializable;

/*
 * �ۼ��� : 14.06.24 
 * �ۼ��� : ������
 * 
 */

public class Payload implements Serializable {

	private int opCode; 
	private int authCode;
	private Snapshot snapshot;
	
	// constuctor
	/**
	 * ������ ��ſ� �̿�Ǵ� payload �� �ʿ��� ������ ����.
	 * @param opCode : �����ڵ� ( 1 : �������� / 2 : ���� �ٿ�ε� / 3 : �������� �б�
	 */
	public Payload(){
		
	}
	
	/**
	 * ������ ��ſ� �̿�Ǵ� payload �� �ʿ��� ������ ����.
	 * @param opCode : �����ڵ� ( 1 : �������� / 2 : ���� �ٿ�ε� / 3 : �������� �б�
	 */
	public Payload(int opCode){
		this.opCode = opCode;
	}
	
	/**
	 * ������ ��ſ� �̿�Ǵ� payload �� �ʿ��� ������ ����.
	 * @param opCode : �����ڵ� ( 1 : �������� / 2 : ���� �ٿ�ε� / 3 : �������� �б�
	 * @param authCode : �����ڵ� ( mobile ���� ���� )
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
