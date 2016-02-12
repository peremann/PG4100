package westerdals.solutions.exercise6;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * For playback, the following site provided help:
 * http://albertattard.blogspot.no/2009/12/simple-java-wav-player.html
 * @author lauper
 *
 */
public class UDPServer {

	public UDPServer() {

		try (DatagramSocket socket = new DatagramSocket(5000)) {
			// set up packet
			int size = 13640;
			byte[] data = new byte[size];
			while (true) {
				DatagramPacket receivePacket = new DatagramPacket(data,
						data.length);
				socket.receive(receivePacket); // wait for packet
				try {
					byte[] dataReceived = receivePacket.getData();
					System.out.println("dataReceived:" + dataReceived.length);
					ByteArrayInputStream in = new ByteArrayInputStream(dataReceived);
					System.out.println("Moooooooooooo");
					play(in);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * http://albertattard.blogspot.no/2009/12/simple-java-wav-player.html
	 */
	private void play(final InputStream inputStream) {
		new Thread() {
			@Override
			public void run() {
				AudioInputStream audioInputStream = null;
				try {
					audioInputStream = AudioSystem
							.getAudioInputStream(inputStream);
				} catch (UnsupportedAudioFileException e) {
					e.printStackTrace();
					return;
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}

				SourceDataLine sourceDataLine = null;
				try {
					AudioFormat audioFormat = audioInputStream.getFormat();
					DataLine.Info info = new DataLine.Info(
							SourceDataLine.class, audioFormat);
					sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
					sourceDataLine.open(audioFormat);
				} catch (LineUnavailableException e) {
					e.printStackTrace();
					return;
				}

				sourceDataLine.start();
				byte[] data = new byte[524288];// 128Kb
				try {
					int bytesRead = 0;
					while (bytesRead != -1) {
						bytesRead = audioInputStream.read(data, 0, data.length);
						if (bytesRead >= 0)
							sourceDataLine.write(data, 0, bytesRead);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				} finally {
					sourceDataLine.drain();
					sourceDataLine.close();
				}
			}
		}.start();
	}

	public static void main(String[] args) {
		new UDPServer();
	}

}
