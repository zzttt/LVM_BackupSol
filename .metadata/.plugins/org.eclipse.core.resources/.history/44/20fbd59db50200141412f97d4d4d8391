package Main;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import FileManager.DirDistributor;
import Functions.FileReceiver;
import Functions.Payload;
import Functions.SocketConnector;

public class SrvMain {
	
	public static String homeDir = "/home/armin/ssHome/";
	public static ServerSocket listenSocket;
	public static Socket connSocket;
	
	public static void main(String args[]) throws Exception{
		
		listenSocket = new ServerSocket(8000);
		
		System.out.println("socket Srv Created");
		
		SrvThread thread1 ;
		BufferedReader in;
		FileOutputStream out;
		DataOutputStream dis;
		ObjectInputStream ois;
		
		DirDistributor fd = new DirDistributor("authCode"); // authCode 는 접속한 사용자에 의해 생성됨
		//fd.mkFolder();
		//fd.delFolder(fd.getImgPath());
				
		while((connSocket = listenSocket.accept()) != null){ //Waiting Connection
			System.out.println(connSocket.getInetAddress()+" Client Connected");
			
			// receive Snapshot Information 
			ois = new ObjectInputStream(connSocket.getInputStream());
			
			SocketConnector sc = new SocketConnector(connSocket,ois);
			sc.start();
						
			//System.out.println("soc end");
		}
		
	}
	
}
