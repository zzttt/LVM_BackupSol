package com.Functions;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.FileManager.FileSender;
import com.example.timetraveler.MainActivity;

public class ConnServer extends Thread {

	Socket sc;
	String authCode;

	String srvIp;
	int port;
	int opCode;

	public ConnServer(String srvIp, int port) {
		this.srvIp = srvIp;
		this.port = port;
	}

	public ConnServer(String srvIp, int port, int opCode) {
		this.srvIp = srvIp;
		this.port = port;
		this.opCode = opCode;
	}

	@Override
	public void run() {
		try {
			// File 전송 절차
			/*
			 * 1. 소켓연결 , FileSender 초기화 2. Opcode 포함한 Payload 를 Object output
			 * Stream으로 전송 전송 3. HomeDir내의 파일 개수를 전송 4. HomeDir내의 파일 정보들을 모두 전송
			 * 5. FileSender를 통한 파일전송
			 */

			// 1
			sc = new Socket(srvIp, port);
			FileSender fs = new FileSender(MainActivity.homePath, this.sc);

			// 2
			Payload pl = new Payload(1);
			ObjectOutputStream oos = new ObjectOutputStream(
					sc.getOutputStream());
			oos.writeObject(pl);

			// 3. HomeDir내의 파일 개수를 전송
			File toSendFile = new File(MainActivity.homePath);
			File[] snapshotList = toSendFile.listFiles();
			int FileCount = 0;

			for (int i = 0; i < snapshotList.length; i++) {
				if (!snapshotList[i].isFile()){
					FileCount++;
				}
			}
			oos.writeObject(FileCount); // 파일 갯수

			// 4. HomeDir내의 파일 정보들을 모두 전송
			for (int i = 0; i < snapshotList.length; i++) {
				if (snapshotList[i].isFile()){
					oos.writeLong(snapshotList[i].length()); // file Size
					oos.writeObject(snapshotList[i]);
					oos.writeObject(snapshotList[i].getName());
				}
			}

			// 5. FileSender를 통한 파일전송

			for (int i = 0; i < snapshotList.length; i++) {
				if (snapshotList[i].isFile()){
					System.out.println("전송할 파일명 : " + snapshotList[i]);
					fs.sendFile(snapshotList[i].getName()); // 파일 전송
				}
			}
			
			sc.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
