package westerdals.solutions.exercise1;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class PortScanner3 {
	public static void main(String args[]) {
		String host = "localhost";
		InetAddress localhost = null;
		try {
			localhost =
					InetAddress.getByName(host);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		int[] possiblePorts = {139, 4370, 4380, 8099, 8100};
		
		for (int i = 0; i < possiblePorts.length; i++) {
			try (Socket s = new Socket(localhost, possiblePorts[i])){
				System.out.println(
						" This is a server port " + possiblePorts[i] + " at " 
								+ host + " " + s.toString());
			} catch (UnknownHostException e) {
				System.out.println("Unknown host");
			} catch (IOException e) {
				System.out.println("Port " + possiblePorts[i] + " could not be opened");
			}
		}
	}
}
