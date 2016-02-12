package westerdals.solutions.exercise1;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class PortScanner2 {
	public static void main(String args[]) {
		String host = "localhost";
		InetAddress localhost = null;
		try {
			localhost =
					InetAddress.getByName(host);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		for (int i = 1025; i < 65536; i++) {
			try (Socket s = new Socket(localhost, i)){
				System.out.println(
						" This is a server port " + i + " at " 
								+ host + " " + s.toString());
			} catch (UnknownHostException e) {
				System.out.println("Unknown host");
			} catch (IOException e) {
				System.out.println("Port " + i + " could not be opened");
			}
		}
	}
}
