package com.Functions;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.FileManager.FileSender;
import com.example.timetraveler.MainActivity;

public class ConnServer extends Thread {

	Socket sc;
	String authCode;

	String srvIp;

	int port;
	int opCode;

	File[] snapshotList;
	Handler andHandler;

	private ObjectInputStream ois;

	public ConnServer(String srvIp, int port) {
		this.srvIp = srvIp;
		this.port = port;
	}

	/**
	 * 
	 * @param srvIp
	 * @param port
	 * @param opCode
	 * @param userCode
	 */
	public ConnServer(String srvIp, int port, int opCode, String userCode) {
		this.srvIp = srvIp;
		this.port = port;
		this.opCode = opCode;
		this.authCode = userCode;
	}
	
	/**
	 * 
	 * @param srvIp
	 * @param port
	 * @param opCode
	 * @param userCode
	 * @param andHandler
	 */

	public ConnServer(String srvIp, int port, int opCode, String userCode,
			Handler andHandler) {
		this.srvIp = srvIp;
		this.port = port;
		this.opCode = opCode;
		this.authCode = userCode;
		this.andHandler = andHandler;
	}

	@Override
	public void run() {
		try {
			sc = new Socket(srvIp, port);
			ObjectOutputStream oos = new ObjectOutputStream(
					sc.getOutputStream());

			// 1 ( 소켓은 이미 앞에서 연결함 )
			FileSender fs = new FileSender(MainActivity.homePath, this.sc);

			// 1 - 1 .. Server에 Connect 시 auth Code 전송
			oos.writeObject(authCode);

			Calendar time = Calendar.getInstance();
			String today = (new SimpleDateFormat("yyyyMMddHHmm").format(time
					.getTime()));
			System.out.println(today);

			// Date 전송
			oos.writeObject(today);
			Payload pl;

			Log.i("eee", "opCode :" + Integer.toString(opCode));
			switch (this.opCode) {
			case 0: // 기기 code 에 따라 정보 조회
				// Snapshot 정보조회
				/*
				 * 1. opcode 포함 payload 전송 2. Snapshot Object 수신
				 */
				// 1.
				pl = new Payload(0);
				oos.writeObject(pl);

				try {
					ois = new ObjectInputStream(sc.getInputStream());

					// 스냅샷 파일 리스트
					int len = ois.readInt();
					snapshotList = new File[len];

					for (int i = 0; i < len; i++) {
						snapshotList[i] = (File) ois.readObject();
					}
					MainActivity.snapshotListInSrv = snapshotList.clone();
					Snapshot ss = (Snapshot) ois.readObject(); // 스냅샷 읽기
					ois.close();
					oos.close();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					Log.e("eee", "Loading error");
					e.printStackTrace();
				} finally {
					// 정보 조회가 끝남을 알림. Snapshot List 업데이트됨
					andHandler.sendEmptyMessage(100);
				}

				break;
			case 1:
				// File 전송 절차
				/*
				 * 1. 소켓연결 , FileSender 초기화(앞에서 미리 실행) 2. Opcode 포함한 Payload 를
				 * Object output Stream으로 전송 전송 3. HomeDir내의 파일 개수를 전송 4.
				 * HomeDir내의 파일 정보들을 모두 전송 5. FileSender를 통한 파일전송
				 */
				// 2
				pl = new Payload(1);
				oos.writeObject(pl);

				// 3. HomeDir내의 파일 개수를 전송
				File toSendFile = new File(MainActivity.homePath);
				File[] snapshotList = toSendFile.listFiles();
				int FileCount = 0;

				for (int i = 0; i < snapshotList.length; i++) {
					if (snapshotList[i].isFile()) {
						FileCount++;
					}
				}
				Log.i("eee", Integer.toString(FileCount));
				oos.writeObject(FileCount); // 파일 갯수

				// 4. HomeDir내의 파일 정보들을 모두 전송
				for (int i = 0; i < snapshotList.length; i++) {
					if (snapshotList[i].isFile()) {
						oos.writeLong(snapshotList[i].length()); // file Size
						oos.writeObject(snapshotList[i]);
						oos.writeObject(snapshotList[i].getName());
					}
				}

				// 5. FileSender를 통한 파일전송
				for (int i = 0; i < snapshotList.length; i++) {
					if (snapshotList[i].isFile()) {
						System.out.println("전송할 파일명 : " + snapshotList[i]);
						fs.sendFile(snapshotList[i].getName()); // 파일 전송
					}
				}

				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
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

	public Socket getSocket() {
		return this.sc;
	}

	public File[] getSsList() {
		return snapshotList;
	}

}
