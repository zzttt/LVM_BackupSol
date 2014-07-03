package FileManager;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.zip.*;

public class GzipGenerator {

	private int cmpPartNumber;
	private BufferedReader in;
	private GZIPOutputStream out;

	public GzipGenerator() {
		this.cmpPartNumber = 1;
	}

	/**
	 * 분할압축 메서드
	 * 
	 * @param src
	 *            : 압축 할 파일
	 * @param destDir
	 *            : 압축된 파일 경로
	 * @throws IOException
	 */
	public void partCompress(String src, String destDir) throws IOException { // 분할압축
		String fileName; // 확장자가 제거된 파일이름
		String fFullName; // 확장자 포함한 파일 명
		String destFile;
		File srcFile = new File(src);
		
		int preIdx = srcFile.getName().lastIndexOf("/");
		int postIdx = srcFile.getName().indexOf(".");
		fileName = srcFile.getName().substring(preIdx+1, postIdx);
		fFullName = srcFile.getName();
		destFile = destDir + fFullName.replace(fileName, fileName+"1") + ".gz";

		in = new BufferedReader(new FileReader(src));
		out = new GZIPOutputStream(new FileOutputStream(destFile));

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
			
			if(writeSize <= limitSize){ // 제한사이즈 이내
				totalWrite += readByte;
				out.write(byteArr, 0, readByte);
			}else{ // 사이즈 초과
				out.write(byteArr, 0, readByte);
				totalWrite += readByte;
				out.close();
				System.out.println("make next File!");
				cmpPartNumber++;
				destFile = destFile.replace((cmpPartNumber-1)+".", cmpPartNumber+".");
				out = new GZIPOutputStream(new FileOutputStream(destFile));
				
				writeSize = 0;
			}
			
		}
		System.out.println(totalWrite);
		out.close(); // 파일을 닫음
		in.close();
	}

	/**
	 * 
	 * @param srcFile
	 * @param destDir
	 *            : directory of destination
	 * @throws IOException
	 */

	public void compress(String srcFile, String destDir) throws IOException { // 단일파일
																				// 압축
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

	/**
	 * 압축해제 메서드 분할된 압축파일을 압축해제하여 하나의 이미지로 만들어낸다.
	 * 
	 * @param src
	 * @param destDir
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void decompress(String srcDir, String destDir)
			throws FileNotFoundException, IOException, InterruptedException {

		File snapshotDir = new File(srcDir);

		if (!snapshotDir.isDirectory()) {
			System.out.println("destDir is not a directory");
			return;
		} else {
			File destList[] = snapshotDir.listFiles();
			Arrays.sort(destList);
			
			String resultFileNm = "snapshot"; // 결과파일 이름은 snapshot으로 떨어트린다.

			System.out.println("file cnt : " + destList.length);

			if (destList[0].getName().contains(".tar")) { // tar 파일의 분할압축일 경우
				resultFileNm = "snapshot.tar";
			}

			FileOutputStream out = new FileOutputStream(destDir + resultFileNm); // dest로 압축 해제 후 출력 (이름은 snapshot으로 )
			long writeSize = 0;
			FileInputStream fis = null;
			GZIPInputStream gis = null;
			
			int totalRead = 0;

			for (int i =  0 ; i < destList.length ; i++) {
				long startTime = System.currentTimeMillis();
				fis = new FileInputStream(destList[i]);
				gis = new GZIPInputStream(fis);
				
				// 분할된 압축파일을 하나로 묶는다.
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

