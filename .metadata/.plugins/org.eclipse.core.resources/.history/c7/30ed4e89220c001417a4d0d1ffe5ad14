package com.Main;

/*
 * 
 * �옉�꽦�옄 : 議곗쁺誘�
 * 理쒖쥌�닔�젙 : 14.07.01
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

import com.AuthcodeGen.CodeGenerator;
import com.FileManager.FileSender;
import com.FileManager.GzipGenerator;
import com.Functions.Payload;
import com.Functions.Snapshot;
import com.Functions.opSwitch;

public class ConnToSrv {

	public static void main(String args[]) {
		String authCode;
		Socket sc;
		
		try {
			sc = new Socket("211.189.19.45", 1234);

			// gg.partCompress("/home/armin/snapshot.tar","/home/armin/snapshot.tar.gz");
			// // src�뙆�씪怨� dest 寃쎈줈瑜� 吏��젙
			ObjectOutputStream oos = new ObjectOutputStream(
					sc.getOutputStream()); // 吏곷젹�솕 媛앹껜 �쟾�넚

			String opCode = null;
			Scanner keyScan = new Scanner(System.in);

			while (true) {
				if (sc.isClosed())
					break;

				System.out.println("insert opCode on Colsole");
				System.out.println("-1 : 醫낅즺 || 0 : noOp || 1 : send snapshot || 2 : download snapshot || 3 : get info || 4 : compress files || 5 : decompress files");
				opCode = keyScan.nextLine();

				opSwitch op = new opSwitch(Integer.parseInt(opCode), oos, sc);
				op.start();

				if (Integer.parseInt(opCode) == -1) {
					break;
				}
			}

			System.out.println("諛섎났醫낅즺");

		} catch (Exception e) {
			System.out.println("exception : " + e.getMessage());
		}

	}

}

