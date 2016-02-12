package westerdals.solutions.exercise5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Initial solution for server in UDP communication.
 * Enhance to complete exercise 5, lab 6 PG4100.
 * @author lauper
 *
 */
public class UDPServer {
	
	public UDPServer(){
		try (DatagramSocket socket = new DatagramSocket(5000)){
			// set up packet
			int size = 20;
			byte[] data = new byte[ size ];
			while(true){
				DatagramPacket receivePacket =   new DatagramPacket( data, data.length );
				socket.receive( receivePacket );  // wait for packet
				String decoded = new String(receivePacket.getData(), "UTF-8");
				System.out.println("Package content:"+decoded);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	public static void main(String[] args){
		new UDPServer();
	}

}
