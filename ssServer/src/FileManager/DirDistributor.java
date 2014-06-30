package FileManager;

import java.io.File;

public class DirDistributor {
	
	final static String folderLoc = "/home/armin/ssHome/"; // snapshot Home Dir
	private String authCode; // Directory 명은 authCode 에 따라 생성됨.
	
	public DirDistributor(){
		
	}
	
	public DirDistributor(String authCode){
		this.authCode = authCode;
	}
	
	public void mkFolder(){ // make folder
		String filePath = folderLoc+authCode;
		System.out.print(filePath);
		File newFile = new File(filePath);
		newFile.mkdir();
	}
	
	public void delFolder(String Path){ // delete file or folder
		String filePath = folderLoc+authCode;
		File f = new File(filePath);
		f.delete();
	}	
	
	public String getImgPath(){ // return location where a img be setted
		String loc = null;
		loc = folderLoc+authCode;
		
		return loc;
	}
	
	
}
