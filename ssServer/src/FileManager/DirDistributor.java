package FileManager;

import java.io.File;

import Main.SrvMain;

public class DirDistributor {
	
	private String authCode; //
	private String getTime;
	private String ssPath;
	
	public DirDistributor(){
		
	}
	
	public DirDistributor(String authCode){
		this.authCode = authCode;
		this.getTime = getTime;
		ssPath =  SrvMain.homeDir+authCode;
	}
	
	public DirDistributor(String authCode,String getTime){
		this.authCode = authCode;
		this.getTime = getTime;
		ssPath =  SrvMain.homeDir+authCode+"/"+getTime;
	}
	
	public boolean IsFolderExist(){
		
		return false;
	}
	
	public void mkFolder(){ // make folder
		System.out.print(ssPath);
		File newFile = new File(ssPath);
		
		if(newFile.mkdirs())
			System.out.println("폴더생성 성공");
		else
			System.out.println("폴더생성 실패");
	}
	
	public void delFolder(String Path){ // delete file or folder
		String filePath = SrvMain.homeDir+authCode;
		File f = new File(filePath);
		f.delete();
	}	
	
	public String getImgPath(){ // return location where a img be setted
		String loc = null;
		loc = ssPath;
		
		return loc;
	}

	/**
	 * authCode 에 따라 해당 기기가 업로드한 모든 스냅샷 리스트를 리턴
	 * @param authCode
	 * @return
	 */
	public File[] getSnapshotDirs(String authCode){
		File f = new File(SrvMain.homeDir+authCode+"/");
		File fDateList[] = f.listFiles();
		
		return fDateList;
	}
	
	
	/**
	 * 특정 날짜에 해당하는 분할된 스냅샷 리스트를 읽어온다
	 * @param authCode
	 * @return
	 */
	public File[] getSepSnapshot(String authCode,String getTime){
		File f = new File( SrvMain.homeDir+authCode+"/"+getTime);
		File fList[] = f.listFiles();
		
		return fList;
	}
	
}
