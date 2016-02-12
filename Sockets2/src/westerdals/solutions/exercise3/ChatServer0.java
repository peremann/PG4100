package westerdals.solutions.exercise3;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChatServer0 extends JFrame {
	private static final long serialVersionUID = 5708029136536183246L;
	private JTextField messageField;
	private JTextArea displayArea;
	private JLabel numberOfClientsLabel;
	private int id = 1000;
	private int numberOfClients = 0;
	private ArrayList<ClientConnection> clients;

	/**
	 * Initializes Server and GUI elements.
	 */
	public ChatServer0() {
		super("Chatserver");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clients = new ArrayList<ClientConnection>(0);
		initMessageField();
		initDisplayArea();
		numberOfClientsLabel = new JLabel("Number of clients connected: 0");
		add(numberOfClientsLabel, BorderLayout.SOUTH);
		setSize(400, 200);
		setVisible(true);
		startServer();
	}

	private void initDisplayArea() {
		displayArea = new JTextArea();
		displayArea.setFont(new Font(null, Font.BOLD, 15));
		add(new JScrollPane(displayArea), BorderLayout.CENTER);
		displayArea.setEditable(false);
	}

	private void initMessageField() {
		messageField = new JTextField();
		messageField.setEditable(true);
		messageField.setFont(new Font(null, Font.BOLD, 15));
		messageField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String message = event.getActionCommand();
				// Sending to all clients
				for (int i = 0; i < clients.size(); i++) {
					ClientConnection client = clients.get(i);
					client.sendData(message);
				}
				displayMessage(message);
			}
		});
		add(messageField, BorderLayout.NORTH);
	}

	private void startServer() {
		try (ServerSocket serverSocket = new ServerSocket(8000)) {
			while (true) {
				Socket socket = serverSocket.accept();
				ClientConnection clientConnection = new ClientConnection(
						socket, ++id);
				clients.add(clientConnection);
				numberOfClients++;
				numberOfClientsLabel.setText("Number of clients connected: "
						+ numberOfClients);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private class ClientConnection implements Runnable {
		private int id;
		private Socket clientContact;
		private ObjectOutputStream output;
		private ObjectInputStream input;

		public ClientConnection(Socket contact, int id) {
			this.id = id;
			this.clientContact = contact;
			new Thread(this).start();
		}

		@Override
		public void run() {
			try {
				initiateContact();
				String message;
				do {
					message = (String) input.readObject();
					displayMessage(message);

				} while (!"end".equals(message));

				numberOfClients--;
				numberOfClientsLabel.setText("Number of clients connected: "
						+ numberOfClients);
				clients.remove(this);
			} catch (IOException | ClassNotFoundException e) {
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

		private void initiateContact() throws IOException {
			output = new ObjectOutputStream(clientContact.getOutputStream());
			output.flush();
			input = new ObjectInputStream(clientContact.getInputStream());
			String message = "You have ID: " + getID();
			sendData(message);
		}

		public int getID() {
			return id;
		}

		public String toString() {
			return "Client " + id;
		}

		public boolean equals(Object o) {
			if (!(o instanceof ClientConnection))
				return false;
			if (o == this)
				return true;
			ClientConnection k = (ClientConnection) o;
			return k.getID() == getID();
		}

		public void sendData(String melding) {
			try {
				output.writeObject(melding);
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void displayMessage(String melding) {
		displayArea.append(melding + "\n");
		displayArea.setCaretPosition(displayArea.getText().length());
	}

	public static void main(String args[]) {
		new ChatServer0();
	}
}