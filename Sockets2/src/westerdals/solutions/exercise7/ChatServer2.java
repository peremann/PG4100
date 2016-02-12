package westerdals.solutions.exercise7;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer2 {
	private int numberOfClients = 0;
	private Vector<ClientConnection> clientConnections;

	public ChatServer2() {
		clientConnections = new Vector<ClientConnection>();
		startServer();
	}

	public void startServer() {
		try (ServerSocket serverSocket = new ServerSocket(8000, 100);) {
			while (true) {
				System.out.println("Waiting for request");
				Socket forbindelse = serverSocket.accept();
				System.out.println("Request " + ++numberOfClients
						+ " received from: "
						+ forbindelse.getInetAddress().getHostName());
				new ClientConnection(forbindelse, numberOfClients);
				System.out.println(clientConnections.size());
			}
		} catch (IOException e) {
		}
	}

	private class ClientConnection implements Runnable {
		private Socket clientContact;
		private ObjectOutputStream output;
		private ObjectInputStream input;
		private String nick, nickKontakt;
		private int id;

		public ClientConnection(String nick, int id) {
			this.nick = nick;
			this.id = id;
		}

		public ClientConnection(Socket clientContact, int id)
				throws IOException {
			this.clientContact = clientContact;
			this.id = id;
			output = new ObjectOutputStream(clientContact.getOutputStream());
			output.flush();
			input = new ObjectInputStream(clientContact.getInputStream());
			new Thread(this).start();
		}

		public String getNick() {
			return nick;
		}

		public void sendMessage(Object message) throws IOException {
			output.writeObject(message);
		}

		public void flush() throws IOException {
			output.flush();
		}

		public void run() {
			try {
				nick = (String) input.readObject();
				if (clientConnections.contains(this)) {
					handleConnectionRequestFromAlreadyExistingClient();
				} else {
					handleNewConnection();
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			} finally{
				try {
					output.close();
					input.close();
					clientContact.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void handleNewConnection() throws IOException,
				ClassNotFoundException {
			clientConnections.add(this);
			Vector<String> allCurrentClientNickNames = getAllExistingClientNicks();
			sendNickOfAllClientsToAllClients(allCurrentClientNickNames);
			String message;
			do {
				message = (String) input.readObject();
				if ("end".equals(message)) {
					handleIncomingEndMessage(allCurrentClientNickNames);
				}
				if (message.startsWith("new")) {
					handleIncomingNewMessage(message);
				} else {
					handleIncomingNormalMessage(message);
				}
				System.out.println(message);
			} while (!"end".equals(message));
		}

		private void handleIncomingNormalMessage(String message)
				throws IOException {
			int index = 0;
			ClientConnection mottager = null;
			while (index < clientConnections.size()) {
				mottager = clientConnections.get(index);
				if (mottager.getNick().equals(nickKontakt))
					break;
				index++;
			}
			System.out.println(index);
			if (index < clientConnections.size()) {
				mottager.sendMessage(nick + " -> " + nickKontakt + " : "
						+ message);
			} else {
				sendMessage("user is not logged in");
			}
		}

		private void handleIncomingNewMessage(String message) {
			nickKontakt = message.substring(4);
			System.out.println(nick + " -> " + nickKontakt);
		}

		private void handleIncomingEndMessage(
				Vector<String> allCurrentClientNickNames) throws IOException {
			boolean loggerUt = clientConnections.remove(new ClientConnection(
					nick, 0));
			if (loggerUt) {
				System.out.println(nick + " logged out");
				numberOfClients--;
				System.out.println(clientConnections);
				sendNickOfAllClientsToAllClients(allCurrentClientNickNames);
			}
		}

		private void sendNickOfAllClientsToAllClients(
				Vector<String> allCurrentClientNickNames) throws IOException {
			for (ClientConnection k : clientConnections) {
				k.sendMessage(allCurrentClientNickNames);
				k.flush();
			}
		}

		private Vector<String> getAllExistingClientNicks() {
			Vector<String> innloggede = new Vector<String>();
			for (ClientConnection k : clientConnections) {
				innloggede.add(k.getNick());
			}
			return innloggede;
		}

		private void handleConnectionRequestFromAlreadyExistingClient()
				throws IOException {
			sendMessage(nick + " is already logged in!");
			clientConnections.remove(this);
			numberOfClients--;
		}

		public String toString() {
			return id + " " + nick;
		}

		public boolean equals(Object o) {
			if (!(o instanceof ClientConnection))
				return false;
			if (o == this)
				return true;
			ClientConnection k = (ClientConnection) o;
			return k.getNick().equals(getNick());
		}

	}

	public static void main(String args[]) {
		new ChatServer2();
	}
}