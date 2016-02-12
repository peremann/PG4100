package westerdals.solutions.exercise5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

/**
 * Initial solution for client in UDP communication.
 * Enhance to complete exercise 5, lab 6 PG4100.
 * @author lauper
 *
 */
public class UDPClient {

	private int port = 5000;
	private String host = "localhost";
	private final int packageSize = 10;
	
	public UDPClient() {
		try (DatagramSocket socket = new DatagramSocket()){
			String message = "this is a very long String with lots of characters, more precisely 86 characters!!!!!!";
			byte[] data = message.getBytes(Charset.forName("UTF-8"));
			System.out.println("data.length:"+data.length);
			DatagramPacket sendPacket;
			for (int i = 0; i < data.length; i = i+packageSize) {
				sendPacket = new DatagramPacket( data, i, Integer.min(packageSize, data.length-i), 
						InetAddress.getByName(host), port );
				socket.send( sendPacket );
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	
	public static void main(String[] args){
		new UDPClient();
	}
}
