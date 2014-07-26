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
import java.util.Date;

import FileManager.DirDistributor;
import Functions.FileReceiver;

import com.FrameWork.Payload;

import Functions.SocketConnector;

public class SrvMain {
	
	final public static String homeDir = "D:/ssServerHome/";
	
	public static String snapshotDir = "D:/ssServerHome/";
	public static ServerSocket listenSocket;
	public static Socket connSocket;
	
	public static void main(String args[]) throws Exception{
		
		listenSocket = new ServerSocket(12345);
		
		System.out.println("socket Srv Created port ("+listenSocket.getLocalPort()+")");
		
		BufferedReader in;
		FileOutputStream out;
		DataOutputStream dis;
		ObjectInputStream ois;
		
		while((connSocket = listenSocket.accept()) != null){ //Waiting Connection
			System.out.println(connSocket.getInetAddress()+" Client Connected");
			ois = new ObjectInputStream(connSocket.getInputStream());
			
			// receive Snapshot Information
			SocketConnector sc = new SocketConnector(connSocket,ois);
			sc.start();
						
			//System.out.println("soc end");
		}
		
	}
	
}
