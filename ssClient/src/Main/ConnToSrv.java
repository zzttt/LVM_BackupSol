package Main;

/*
 * 
 * 작성자 : 조영민
 * 최종수정 : 14.07.01
 * 
 */
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import AuthcodeGen.CodeGenerator;
import FileManager.FileSender;
import FileManager.GzipGenerator;
import Functions.Payload;
import Functions.Snapshot;

public class ConnToSrv {

	public static void main(String args[]) {
		String authCode;
		Socket sc;
		
		try {
			sc = new Socket("211.189.19.45", 8000);

			// gg.partCompress("/home/armin/snapshot.tar","/home/armin/snapshot.tar.gz");
			// // src파일과 dest 경로를 지정
			ObjectOutputStream oos = new ObjectOutputStream(
					sc.getOutputStream()); // 직렬화 객체 전송

			String opCode = null;
			Scanner keyScan = new Scanner(System.in);

			while (true) {
				if (sc.isClosed())
					break;

				System.out.println("insert opCode on Colsole");
				opCode = keyScan.nextLine();

				opSwitch op = new opSwitch(Integer.parseInt(opCode), oos, sc);
				op.start();

				if (Integer.parseInt(opCode) == -1) {
					break;
				}
			}

			System.out.println("반복종료");

		} catch (Exception e) {
			System.out.println("exception : " + e.getMessage());
		}

	}

}

class opSwitch extends Thread {
	private int opCode;
	private ObjectOutputStream oos;
	private Socket opSocket;
	private static int BUFFSIZE = 1024;

	public opSwitch(int opCode, ObjectOutputStream oos, Socket opSocket) {
		this.opCode = opCode;
		this.oos = oos;
		this.opSocket = opSocket;
	}

	@Override
	public void run() {
		Payload pl = new Payload(opCode);
		try {
			oos.writeObject(pl);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		switch (opCode) {
		case -1:
			break;
		case 0:

			break;
		case 1: // send files
			System.out.println("file upload");
			try {

				Snapshot ss = new Snapshot(); // 스냅샷 생성

				pl.setSnapshot(ss); // payload에 snapshot 을 담는다.

				// snapshot 정보 전송

				FileSender fs = new FileSender("/home/armin/temp/", opSocket); // 파일이
																				// 존재하는
																				// 디렉토리
																				// 설정
				File hDir = fs.getHomeDir(); // FileSender 의 홈 디렉토리를 가져옴

				File fileList[] = hDir.listFiles(); // 디렉토리 내 파일 리스트
				int fileCnt = 0;

				if (fileList.length != 0) {
					oos.writeObject(Integer.toString(fileList.length)); // 전체 파일
																		// 개수정보
																		// 전송
					while (fileList.length > fileCnt) {
						oos.writeLong(fileList[fileCnt].length()); // file size
																	// 전송
						oos.writeObject(fileList[fileCnt]); // File 정보들은 먼저 보낸다
						oos.writeObject(fileList[fileCnt].getName()); // File
																		// 정보(이름)들은
																		// 먼저
																		// 보낸다
						fileCnt++;
					}

					fileCnt = 0;

					while (fileList.length > fileCnt) {
						System.out.println("전송할 파일명 : " + fileList[fileCnt]);
						fs.sendFile(fileList[fileCnt].getName()); // 파일 전송
						fileCnt++;
					}
				} else {
					oos.writeObject(Integer.toString(fileList.length)); // 전체 파일
																		// 개수정보
																		// 전송
					System.out.println("File is not exist");
					return; // 파일이 존재하지 않으면 socket 종료
				}

				opSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 2:
			System.out.println("file download");
			try {
				ObjectInputStream ois = new ObjectInputStream(
						opSocket.getInputStream());

				// op 2 - get file list
				File[] fileList = (File[]) ois.readObject(); // 서버에 등록된 파일 리스트를
																// 받는다.

				System.out.println("다운 할 파일 수 : " + fileList.length);

				int fileCnt = 0;
				while (fileList.length > fileCnt) {
					System.out.println(fileList[fileCnt].getName());
					fileCnt++;
				}
				fileCnt = 0;

				while (fileList.length > fileCnt) {
					File wFile = new File("/home/armin/ssHome/downloads/"
							+ fileList[fileCnt].getName()); // set home
															// directory on
															// client
					FileOutputStream fos = new FileOutputStream(wFile); // 다운로드
																		// 할 파일을
																		// 입력할
																		// 스트림
					if (fileList[fileCnt].isFile()) {
						byte[] byteArr = new byte[1024];
						int readBytes = 0;
						long fileSize = 0;
						System.out.println("receiving file name "
								+ fileList[fileCnt].getName());
						System.out.println("file path to be written "
								+ wFile.getPath());

						InputStream is = opSocket.getInputStream();

						while (fileSize + BUFFSIZE < fileList[fileCnt].length()) {

							if ((readBytes = is.read(byteArr)) != -1)
								fileSize += readBytes;
							fos.write(byteArr, 0, readBytes);

						}

						if (fileSize + BUFFSIZE >= fileList[fileCnt].length()) { // 다음버퍼를
																					// 읽을시
																					// 용량을
																					// 초과할
																					// 경우
							byte[] tmpBuf;

							int overSize = (int) (fileSize + BUFFSIZE - fileList[fileCnt]
									.length());
							int tempBufSize = BUFFSIZE - overSize;
							tmpBuf = new byte[tempBufSize];

							if ((readBytes = is.read(tmpBuf)) != -1) {

								System.out.println("readBytes : " + readBytes);
								fileSize += readBytes;

								System.out.println("total size : " + fileSize);
								fos.write(tmpBuf, 0, readBytes);
							}
						} else {

						}

						fileCnt++;

						fos.flush();
						fos.close();
					} else { // if wFile is a directory
						fileCnt++;
						wFile.delete();
						fos.flush();
						fos.close();
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("insert opCode on Colsole");
			break;
		case 3:
			System.out.println("request file info");
			
			
			break;
		case 4:// compress files
			System.out.println("file compressing");
			// 분할압축 및 해제
			GzipGenerator ggForComp = new GzipGenerator();
			try {
				ggForComp.partCompress("/home/armin/ssHome/capstone.tar",
						"/home/armin/ssHome/comp/");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			break;
		case 5: // decompress a file
			System.out.println("decompress a file");
			GzipGenerator ggForDeComp = new GzipGenerator();
			try {
				ggForDeComp.decompress("/home/armin/ssHome/comp/",
						"/home/armin/ssHome/decomp/");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		case 6:
			System.out.println("code Generate");
			String code1,code2;
			
			CodeGenerator cg = new CodeGenerator("devCode", "additionalCode");
			
			break;
		}
	}

}