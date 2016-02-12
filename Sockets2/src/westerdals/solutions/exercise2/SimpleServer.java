package westerdals.solutions.exercise2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {

	public SimpleServer() {
		startServer();
	}

	private void startServer() {
		try (ServerSocket server = new ServerSocket(8000);) {
			while (true) {
				System.out.println("Waiting for client request...");
				try (Socket clientConnection = server.accept();
						ObjectInputStream input = new ObjectInputStream(
								clientConnection.getInputStream())) {
					System.out.println("Connection etablished...");
					System.out.println("Client address: "
							+ clientConnection.getInetAddress());
					receive(input);
					System.out.println("Server - Closing connection.");
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void receive(ObjectInputStream input) throws IOException,
			ClassNotFoundException {
		try {
			// Server starts by sende a welcome message
			PersonEdited person = (PersonEdited) input.readObject();
			System.out.println("Person:" + person.toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception:" + e.getMessage());
		}
	}

	public static void main(String args[]) {
		new SimpleServer().startServer();
	}

} // end class SimpleServer
