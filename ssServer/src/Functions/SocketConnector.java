package Functions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Main.SrvMain;

public class SocketConnector extends Thread {

	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public SocketConnector(Socket socket) {
		this.socket = socket;
	}

	public SocketConnector(Socket socket, ObjectInputStream ois) {
		this.socket = socket;
		this.ois = ois;
	}

	@Override
	public void run() {

		while (true && socket.isConnected()) {
			try {
				// read payloads
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
					System.out.println("no Operation");
					break;
				case 1:
					int fileLength = Integer.parseInt(ois.readObject()
							.toString()); // 파일
											// 개수를
											// 읽어옴

					if (fileLength != 0) {

						FileReceiver fr = new FileReceiver(SrvMain.homeDir,
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
					// client 에 fileList 전송
					File homeDir = new File(SrvMain.homeDir);
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
									fileList[fileCnt]); // 파일을 순서대로 읽어온다

							System.out.println("보내는 파일 : "
									+ fileList[fileCnt].getPath());
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
					break;
				case 3:
					
					break;
					
				case 4:
					
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
			}
		}

		System.out.println("end to receive"); // 수신종료

	}

}
