package westerdals.solutions.exercise6;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient {

	private int port = 5000;
	private String host = "localhost";

	public UDPClient() {
		try (DatagramSocket socket = new DatagramSocket();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				BufferedInputStream in = new BufferedInputStream(
						new FileInputStream("cow3.wav"))) {

			int read;
			byte[] buff = new byte[1024];
			while ((read = in.read(buff)) > 0) {
				out.write(buff, 0, read);
			}
			out.flush();
			byte[] audioBytes = out.toByteArray();

			System.out.println("Length:" + audioBytes.length);
			DatagramPacket sendPacket = new DatagramPacket(audioBytes,
					audioBytes.length, InetAddress.getByName(host), port);
			socket.send(sendPacket);
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new UDPClient();
	}
}
