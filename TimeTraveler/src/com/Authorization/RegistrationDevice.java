package com.Authorization;

import com.FrameWork.ConnServer;
import com.example.timetraveler.MainActivity;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.widget.Toast;

public class RegistrationDevice {

	private String userCode; // ����� �ڵ�
	private Handler handler; // �ڵ鷯

	/**
	 * 
	 * @param mng : wifiManager
	 */
	public RegistrationDevice(WifiManager mng, Handler handler) {

		WifiInfo info = mng.getConnectionInfo();

		// MAC �̿� �����ڵ� ����
		CodeGenerator cg = new CodeGenerator(info.getMacAddress());

		// ����� �ڵ� ����
		userCode = cg.genCode();
		
		this.handler = handler;

	}

	public String getUserCode() {
		return this.userCode;
	}

	/**
	 * ���Ͽ� ���� �� ����� ��� ���� Ȯ��
	 */
	public boolean chkUserOnSrv() { // ������ ����� ��Ͽ��� Ȯ��

		userCode = this.getUserCode(); // ����ڵ�

		ConnServer conn = new ConnServer(MainActivity.srvIp, 12345, 3, userCode , handler);
		conn.start();

		return false;
	}

	/**
	 * ����� ���� ����
	 */
	public void createUser() { // ����� ���� ����
		
		ConnServer conn = new ConnServer(MainActivity.srvIp , 12345 , 4 , userCode, handler);
		conn.start();

	}

	/**
	 * ����� ���� �б�
	 */
	public void getUserInfo() {
		ConnServer conn = new ConnServer(MainActivity.srvIp , 12345 , 5, userCode, handler);
		conn.start();
	}

}