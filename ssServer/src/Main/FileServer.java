package Main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {

	public static void main(String[] args) {
		int port = 8000;

		try {
			ServerSocket ss = new ServerSocket(port);
			System.out.println("This server is listening... (Port: " + port
					+ ")");
			
			
			Socket socket = ss.accept();
			InetSocketAddress isaClient = (InetSocketAddress) socket
					.getRemoteSocketAddress();
			String clientAddress = isaClient.getAddress().getHostAddress();
			int clientPort = isaClient.getPort();

			System.out.println("A client is connected. (" + clientAddress + ":"
					+ clientPort + ")");

			// String filename = "test2.txt";
			String filename = "/home/armin/sented.tar";
			FileOutputStream fos = new FileOutputStream(filename);

			InputStream is = socket.getInputStream();

			// int c;
			// while ((c = is.read()) != -1) {
			// // System.out.println("readByte: " + readByte);
			//
			// fos.write(c);
			// }

			byte[] buffer = new byte[1024];
			int readBytes;
			while ((readBytes = is.read(buffer)) != -1) {
				fos.write(buffer, 0, readBytes);
			}

			System.out.println("File transfer completed.");

			is.close();
			fos.close();

			socket.close();
			ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}