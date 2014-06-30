package AuthcodeGen;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * 작성자 : 조영민
 * 작성일 : 14.06.26
 * 
 * Code generator
 * 장치정보를 이용한 코드생성 ( 고유값으로 생성 )
 */
public class CodeGenerator {
	private String devCode;
	private String addCode;
	
	public CodeGenerator(String devCode, String addCode){
		this.devCode = devCode;
		this.addCode = addCode;
	}
	
	public String genCode(){ // creates MD5 hash 
		String md5 = "";
		String mixedCode = devCode+addCode;
		
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
