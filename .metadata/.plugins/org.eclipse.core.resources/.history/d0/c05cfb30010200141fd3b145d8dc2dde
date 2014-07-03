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
import Functions.opSwitch;

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
				System.out.println("-1 : 종료 || 0 : noOp || 1 : send snapshot || 2 : download snapshot || 3 : get info || 4 : compress files || 5 : decompress files");
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

