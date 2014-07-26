package Functions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.FrameWork.Payload;
import com.FrameWork.Snapshot;

import FileManager.DirDistributor;
import Main.SrvMain;


public class SocketConnector extends Thread {

	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private String authCode = null, getTime = null ;
	public SocketConnector(Socket socket) {
		this.socket = socket;
	}

	public SocketConnector(Socket socket, ObjectInputStream ois) {
		this.socket = socket;
		this.ois = ois;
	}

	@Override
	public void run() {
		//authCode , time check
		try {
			authCode = (String)ois.readObject(); // authCode 를 client로부터 받음
			
			// authCode 에 따르는 directory가 존재하는지 확인.
			File devDirectory = new File(SrvMain.homeDir+authCode);
			
			
			if(devDirectory.exists()){ // 해당 디렉터리에 폴더가 있는지 확인 ( 기기 등록이 되었는지 확인 )
				
				
				
				
			}else{ // if a device isn't registered
				
				
				
			}
			
			
			
			
			getTime = (String)ois.readObject();
			
			System.out.println("authCode and getTime :" +authCode +"//"+getTime);
		} catch (ClassNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		
		while (true && socket.isConnected()) {
			try {
				// read a payload . 
				// 
				Payload pl = (Payload) ois.readObject();
				System.out.println("payload opcode : " + pl.getOpCode());
				if (pl.getOpCode() == -1) {
					System.out.println("client left on server");
					ois.close();
					socket.close();
					return;
				}
				
				switch (pl.getOpCode()) {
				case 0:
					System.out.println("Snapshot Info request");
					//authCode내의 Snapshot Info Read
					File f = new File(SrvMain.homeDir+authCode);
					Snapshot ss;
					
					oos = new ObjectOutputStream(socket.getOutputStream());
					
					if(f.exists()){ // 기기정보 인증. 해당 기기정보가 없으면 새로운 디렉토리를 생성한다.
						System.out.println("기기정보 확인 완료, Snapshot 확인 ");
						
						//해당 기기에 등록된 Snapshot 정보들 
						System.out.println("path : "+f.getPath());
						File[] inDirFile = f.listFiles();

						System.out.println("dirFileCnt : "+inDirFile.length);
						
						// 날짜별 스냅샷 정보를 전송
						oos.writeInt(inDirFile.length);
						
						for(int j = 0 ; j < inDirFile.length ; j++){
							System.out.println("fileName : "+inDirFile[j].getName());
							oos.writeObject(inDirFile[j]);
						}
						
						/*for(int i = 0 ; i < inDirFile.length ; i++){
							
							File SnapshotDir = new File(inDirFile[i].getPath());
							
							File[] sepSnapshot = SnapshotDir.listFiles();
							
							System.out.println("sepSnapshotSize : "+sepSnapshot.length);
							
							//ss = new Snapshot();
							
						}*/
				
						// code 디렉토리가 존재 시 날짜 확인
						/*f = new File(SrvMain.homeDir+authCode+"/"+getTime);
						
						if(f.exists()){ // 해댕 날짜의 데이터가 존재하는지 확인
							
						}else{
							
						}*/
						ss = new Snapshot();
						
					}else{
						System.out.println("기기 정보가 존재하지 않습니다. 새로 생성합니다.");
						if(f.mkdir())
						{
							System.out.println("정상적으로 생성되었습니다.");
						}
						else
						{
							System.out.println("생성 실패.");
						}
							
						ss = new Snapshot();
						return;
					}
					
					// snapshot object 전송
					
					oos.writeObject(ss); // snapshot 정보 전송
					ois.close();
					oos.close();
					System.out.println("send info end");
					break;
				case 1:
					int fileLength = Integer.parseInt(ois.readObject()
							.toString()); 
					//인증코드 읽고 디렉토리 확인 및 생성
					DirDistributor dd = new DirDistributor(authCode, getTime);
					
					// snapshotDir (스냅샷이 저장 될 디렉토리 세팅 )
					SrvMain.snapshotDir = dd.getImgPath()+"/" ;
					
					if(dd.IsFolderExist()){ // 스냅샷 존재시

					}else{ // 처음 전송되는 스냅 샷
						// 전송 될 폴더 생성
						dd.mkFolder();
					}
					
					if (fileLength != 0) {
						System.out.println(SrvMain.snapshotDir);
						FileReceiver fr = new FileReceiver(SrvMain.snapshotDir,
								socket, ois, fileLength);
						fr.ReceiveFile();
						
					} else {
						System.out.println("there is no file to receive");
						System.out.println("socket Closed");
						ois.close();
						socket.close();
					}
					return;
				case 2: // file download
					System.out
							.println("server'll send a bunch of files to client");
					
					File homeDir = new File(SrvMain.snapshotDir);
					File[] fileList = homeDir.listFiles();
					int fileCnt = 0;

					System.out
					.println("client에 전송 할 파일 수 : " + fileList.length);

					while (fileList.length > fileCnt) {
						System.out.println(fileList[fileCnt].getName());
						fileCnt++;
					}
					
					fileCnt = 0;

					oos = new ObjectOutputStream(socket.getOutputStream());
					// op 2 - send file list
					oos.writeObject(fileList);

					while (fileList.length > fileCnt) {
						if (fileList[fileCnt].isFile()) {
							FileInputStream fis = new FileInputStream(
									fileList[fileCnt]); //

							
							byte[] byteArr = new byte[1024];
							int readByte = 0;
							long fileSize = 0;

							while ((readByte = fis.read(byteArr)) > 0) {
								fileSize += readByte;
								socket.getOutputStream().write(byteArr, 0,
										readByte);
							}

							fileSize = 0;
							fis.close();
							fileCnt++;
						} else {
							fileCnt++;
						}
					}
					
					oos.close();
					break;
				case 3: // check user
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					
					// read generated code from client
					
					// send Payload with snapshot Information as per the code
					
					break;
					
				case 4: // add user
					
					break;
				}

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				return;
			}
		}

		System.out.println("end to receive"); // �닔�떊醫낅즺

	}

}
