package westerdals.solutions.exercise1;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class PortScanner {
	public static void main(String args[]) {
		String host = "localhost";
		for (int i = 1; i < 1024; i++) {
			try (Socket s = new Socket(host, i)){
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
