package Functions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.util.ArrayList;


public class FileReceiver{
	
	static int BUFFSIZE = 1024;
	private String path;
	private Socket socket;
	private String name;
	private ObjectInputStream ois;
	private int FileLength;
	public FileReceiver(){
		
	}
	/**
	 * 
	 * @param path
	 * @param name
	 * @param socket
	 */
	public FileReceiver(String path,Socket socket,ObjectInputStream ois,int fileLength){
		this.path = path;
		this.socket = socket;
		this.ois = ois;
		this.FileLength  = fileLength;
	}
	
	public void ReceiveFile() {
		FileOutputStream fos;
		try {
//
		//	String op = ois.readObject().toString();
			String fileName = name;
			ArrayList<File> listFiles = new ArrayList(); 
			
			System.out.println("파일개수 : "+FileLength);
			
			if(FileLength != 0){
				ArrayList<Long> FileSize = new ArrayList<Long>();
				ArrayList FileNames = new ArrayList();
				
				System.out.println("파일개수 : "+FileLength);
				
				for(int i = 0 ; i < FileLength ; i++){
					try {
						FileSize.add(ois.readLong()); // fileSize
						System.out.println("파일크기 : "+FileSize.get(i));
						listFiles.add((File) ois.readObject()); // file Object
						FileNames.add(path+ois.readObject().toString()); // FileNames
						
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			//	System.out.println("operation : " + op); // 파일전송 시작
			//	fileName = ois.readObject().toString();
				InputStream is = socket.getInputStream();
				
				byte[] buffer = new byte[BUFFSIZE];
				for(int i = 0 ; i < FileLength ; i++){
					
					int readBytes;
					long totalReadBytes = 0;

					fos = new FileOutputStream(FileNames.get(i).toString());
					System.out.println("fileName : " + FileNames.get(i) + "(" + (long)FileSize.get(i) +")"); // 파일이름
					System.out.println("-----------------------------------------------");

					while (totalReadBytes + BUFFSIZE < (long)FileSize.get(i) ) {
						
						if((readBytes = is.read(buffer)) != -1 ) 
							totalReadBytes += readBytes;
							fos.write(buffer, 0, readBytes);
							
						//System.out.println(totalReadBytes+"/"+listFiles.get(i).length());
					}
					
					if(totalReadBytes + BUFFSIZE >= (long)FileSize.get(i) ){ // 다음버퍼를 읽을시 용량을 초과할 경우
						byte[] tmpBuf;
						System.out.println(totalReadBytes+"//"+BUFFSIZE+"//"+(long)FileSize.get(i));
						
						int overSize = (int) (totalReadBytes + BUFFSIZE - FileSize.get(i));
						int tempBufSize = BUFFSIZE - overSize;
						tmpBuf = new byte[tempBufSize];
						
						if((readBytes = is.read(tmpBuf)) != -1){

							System.out.println("readBytes : " + readBytes);
							totalReadBytes += readBytes;

							System.out.println("total size : " + totalReadBytes);
							fos.write(tmpBuf, 0, readBytes);
						}
					}else{
						
						
					}
					

					fos.flush();
					fos.close();
				}
				
				System.out.println("File receiving completed.");
			}
			
			
			
			// socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
