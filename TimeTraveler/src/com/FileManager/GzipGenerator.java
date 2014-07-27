package com.FileManager;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.zip.*;

import android.util.Log;

public class GzipGenerator {

	private int cmpPartNumber;
	private BufferedReader in;
	private InputStream is;
	private OutputStream os;
	private ObjectOutputStream oos;
	private GZIPOutputStream gOut;

	
	public GzipGenerator() {
		this.cmpPartNumber = 1;
	}
	
	public GzipGenerator(InputStream is) {
		this.cmpPartNumber = 1;
		this.is = is;
	}

	/**
	 * ��Ʈ���� �����ؼ� ������ ������.
	 * @param srvIp
	 * @throws IOException
	 */
	public void SendCompImgToSrv(String srvIp,int port){ 
		byte buffer[] = new byte[1024];
		int size = 0;
		long totalSize = 0;
		Socket sc;
		try {
			//   ++++++++++ sc = new Socket(srvIp, port);

			FileSender fs; // ��Ʈ�� ������ ������ File Sender

			//   ++++++++++ os = sc.getOutputStream();
			//   ++++++++++ oos = new ObjectOutputStream(os);
			
			/*if (sc.isConnected()) { // ���� ����� ��������
				fs = new FileSender(sc);
			} else {
				return;
			}*/
			//   ++++++++++			//   ++++++++++			//   ++++++++++
			
			
			
			/*
			 * input stream ���� �о� socket ���� ���.
			 */
			while ((size = is.read(buffer)) == 0) { // size == 0 �� ���� ���
				// do nothing
			}
			// while���� ������ �Ѱ��� ���۸� �о���δ�.
			
			// ó�� ���� buffer ����
		//   ++++++++++		 gOut = new GZIPOutputStream(os);
			
			//oos.writeInt(size); // ������ byteSize ����
			
			//gOut.write(buffer); // �����ؼ� ������ �ٷ� ���
			
			Log.d("lvm", Integer.toString(size));
			totalSize+=size;
			// while ((size = is.read(buffer)) > 0) {
			
			// avail  > input stream�� �����ִ� ����Ʈ
			int avail ;
			
			while ((avail = is.available()) > 0 && (size = is.read(buffer)) > 0) {
				//Log.e("lvm2", "in while (avail : "+avail+")");
				
				if(avail < 2048){
					// ������ ���� ( input stream �� �� ��� ������� ���� )
					try {
						Thread.sleep(150);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// buffer 2 ~ end ���� ������ ����
				//gOut.write(buffer); // �����ؼ� ������ �ٷ� ���
				
				totalSize += size;
				Log.d("lvm", Long.toString(totalSize));
				
				// Log.d("lvm", "size : " +
				// Long.toString(totalSize/1024/1024)
				// );
			}
			Log.e("lvm2", "out of while");
		//   ++++++++++		 gOut.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			Log.d("lvm", "total : " + Long.toString(totalSize / 1024 / 1024)+ "mb");
		}
		// ������ ����
		/*BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String l;
		while(( l = br.readLine()) == null){ // size == 0�ϵ��� ���	

			Log.d("thread", "waiting .." ); 
		}

		Log.d("lvm", l );
		while(( l = br.readLine()) != null){
			Log.d("lvm", l );
		}*/
	}
	
	public void partCompress(String src, String destDir) throws IOException {
		String fileName;
		String fFullName;
		String destFile;
		File srcFile = new File(src);
		
		int preIdx = srcFile.getName().lastIndexOf("/");
		int postIdx = srcFile.getName().indexOf(".");
		fileName = srcFile.getName().substring(preIdx+1, postIdx);
		fFullName = srcFile.getName();
		
		destFile = destDir + fFullName.replace(fileName, fileName+"1") + ".gz";

		in = new BufferedReader(new FileReader(src));
		gOut = new GZIPOutputStream(new FileOutputStream(destFile));
		
		
		FileInputStream fis = new FileInputStream(srcFile);

		System.out.println(srcFile.length());
		System.out.println("fFullName : "+fFullName);
		System.out.println("destFile : "+destFile);
		String s;
		byte[] byteArr = new byte[1024];
		long writeSize = 0;
		long totalSize = srcFile.length();
		int readByte = 0;
		int totalWrite = 0;
		
		int limitSize =  1024*1024*2;

		while (0 < (readByte = fis.read(byteArr))) {
			writeSize += readByte;
			//System.out.println(writeSize + "/" + totalSize + "/" + limitSize);
			
			if(writeSize <= limitSize){
				totalWrite += readByte;
				gOut.write(byteArr, 0, readByte);
			}else{
				gOut.write(byteArr, 0, readByte);
				totalWrite += readByte; 
				gOut.close();
				System.out.println("make next File!");
				cmpPartNumber++;
				destFile = destFile.replace((cmpPartNumber-1)+".", cmpPartNumber+".");
				gOut = new GZIPOutputStream(new FileOutputStream(destFile));
				
				writeSize = 0;
			}
			
		}
		System.out.println(totalWrite);
		gOut.close(); 
		in.close();
	}

	/**
	 * 
	 * @param srcFile
	 * @param destDir
	 *            : directory of destination
	 * @throws IOException
	 */

	public void compress(String srcFile, String destDir) throws IOException { 
		
		System.out.println(srcFile);

		String destFile = destDir + "snapshot.gz";

		long startTime = startTime = System.currentTimeMillis();

		BufferedReader in = new BufferedReader(new FileReader(srcFile));
		BufferedOutputStream out = new BufferedOutputStream(
				new GZIPOutputStream(new FileOutputStream(destFile)));

		String s;
		while (null != (s = in.readLine())) {
			out.write(s.getBytes());
			out.write("\n".getBytes());
		}
		in.close();
		out.close();

		long endTime = System.currentTimeMillis();
		System.out.println("compress elapsed time : " + (endTime - startTime)
				+ " ms");
	}

	public void decompress(String srcDir, String destDir)
			throws FileNotFoundException, IOException, InterruptedException {

		File snapshotDir = new File(srcDir);

		if (!snapshotDir.isDirectory()) {
			System.out.println("destDir is not a directory");
			return;
		} else {
			File destList[] = snapshotDir.listFiles();
			Arrays.sort(destList);
			
			String resultFileNm = "snapshot"; 

			System.out.println("file cnt : " + destList.length);

			if (destList[0].getName().contains(".tar")) { // tar 
				resultFileNm = "snapshot.tar";
			}

			FileOutputStream out = new FileOutputStream(destDir + resultFileNm); // dest
			long writeSize = 0;
			FileInputStream fis = null;
			GZIPInputStream gis = null;
			
			int totalRead = 0;

			for (int i =  0 ; i < destList.length ; i++) {
				long startTime = System.currentTimeMillis();
				fis = new FileInputStream(destList[i]);
				gis = new GZIPInputStream(fis);
				
				byte[] byteArr = new byte[1024];

				int readByte = 0;
				long fileSize = 0;

				while (0 < (readByte = gis.read(byteArr))) {
					writeSize += readByte;
					fileSize += readByte;
					totalRead += readByte;
					out.write(byteArr, 0, readByte);
				}
				System.out.println("name : "+ destList[i].getName()+" writeSize : " + fileSize);
				fis.close();
				gis.close();
			}
			System.out.println(totalRead);
			out.close();

		}

	}
}

