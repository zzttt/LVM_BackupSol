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

			// 1 ( ������ �̹� �տ��� ������ )
			FileSender fs = new FileSender(MainActivity.homePath, this.sc);

			// 1 - 1 .. Server�� Connect �� auth Code ����
			oos.writeObject(authCode);

			Calendar time = Calendar.getInstance();
			String today = (new SimpleDateFormat("yyyyMMddHHmm").format(time
					.getTime()));
			System.out.println(today);

			// Date ����
			oos.writeObject(today);
			Payload pl;

			Log.i("eee", "opCode :" + Integer.toString(opCode));
			switch (this.opCode) {
			case 0: // ��� code �� ���� ���� ��ȸ
				// Snapshot ������ȸ
				/*
				 * 1. opcode ���� payload ���� 2. Snapshot Object ����
				 */
				// 1.
				pl = new Payload(0);
				oos.writeObject(pl);

				try {
					ois = new ObjectInputStream(sc.getInputStream());

					// ������ ���� ����Ʈ
					int len = ois.readInt();
					snapshotList = new File[len];

					for (int i = 0; i < len; i++) {
						snapshotList[i] = (File) ois.readObject();
					}
					MainActivity.snapshotListInSrv = snapshotList.clone();
					Snapshot ss = (Snapshot) ois.readObject(); // ������ �б�
					ois.close();
					oos.close();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					Log.e("eee", "Loading error");
					e.printStackTrace();
				} finally {
					// ���� ��ȸ�� ������ �˸�. Snapshot List ������Ʈ��
					// looper �ʿ��Ѱ�?
					andHandler.post(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							andHandler.sendEmptyMessage(100);
						}
						
					});
				}

				break;
			case 1:
				// File ���� ����
				/*
				 * 1. ���Ͽ��� , FileSender �ʱ�ȭ(�տ��� �̸� ����) 2. Opcode ������ Payload ��
				 * Object output Stream���� ���� ���� 3. HomeDir���� ���� ������ ���� 4.
				 * HomeDir���� ���� �������� ��� ���� 5. FileSender�� ���� ��������
				 */
				// 2
				pl = new Payload(1);
				oos.writeObject(pl);

				// 3. HomeDir���� ���� ������ ����
				File toSendFile = new File(MainActivity.homePath);
				File[] snapshotList = toSendFile.listFiles();
				int FileCount = 0;

				for (int i = 0; i < snapshotList.length; i++) {
					if (snapshotList[i].isFile()) {
						FileCount++;
					}
				}
				Log.i("eee", Integer.toString(FileCount));
				oos.writeObject(FileCount); // ���� ����

				// 4. HomeDir���� ���� �������� ��� ����
				for (int i = 0; i < snapshotList.length; i++) {
					if (snapshotList[i].isFile()) {
						oos.writeLong(snapshotList[i].length()); // file Size
						oos.writeObject(snapshotList[i]);
						oos.writeObject(snapshotList[i].getName());
					}
				}

				// 5. FileSender�� ���� ��������
				for (int i = 0; i < snapshotList.length; i++) {
					if (snapshotList[i].isFile()) {
						System.out.println("������ ���ϸ� : " + snapshotList[i]);
						fs.sendFile(snapshotList[i].getName()); // ���� ����
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