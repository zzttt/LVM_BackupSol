package com.AuthcodeGen;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * �옉�꽦�옄 : 議곗쁺誘�
 * �옉�꽦�씪 : 14.06.26
 * 
 * Code generator
 * �옣移섏젙蹂대�� �씠�슜�븳 肄붾뱶�깮�꽦 ( 怨좎쑀媛믪쑝濡� �깮�꽦 )
 */
public class CodeGenerator {
	private String macAddr;
	private String addCode;
	
	public CodeGenerator(String macAddr){
		this.macAddr = macAddr;
	}
	public CodeGenerator(String macAddr, String addCode){
		this.macAddr = macAddr;
		this.addCode = addCode;
	}
	
	public String genCode(){ // creates MD5 hash 
		String md5 = "";
		String mixedCode = macAddr+addCode;
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(mixedCode.getBytes());
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer(); 
			
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			md5 = sb.toString();
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return md5.substring(0, 15);
	}
	
}