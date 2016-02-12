package westerdals.solutions.exercise4;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ChatServer1 extends JFrame implements ListSelectionListener {
	private static final long serialVersionUID = 5708029136536183246L;
	private JTextField messageField;
	private JTextArea displayArea;
	private DefaultListModel<ClientConnection> clientModel;
	private ClientConnection chosenClient;
	private JList<ClientConnection> clientList;
	private JLabel numberOfClientsLabel;
	private int id = 1000;
	private int numberOfClients = 0;

	public ChatServer1() {
		super("Chatserver");
		chosenClient = null;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initMessageField();
		initDisplayArea();
		numberOfClientsLabel = new JLabel("Number of clients connected: 0");
		add(numberOfClientsLabel, BorderLayout.SOUTH);
		initList();
		setSize(400, 200);
		setVisible(true);
		start();
	}

	private void initList() {
		clientModel = new DefaultListModel<ClientConnection>();
		clientList = new JList<ClientConnection>(clientModel);
		clientList.addListSelectionListener(this);
		add(clientList, BorderLayout.EAST);
	}

	private void initDisplayArea() {
		displayArea = new JTextArea();
		displayArea.setFont(new Font(null, Font.BOLD, 15));
		add(new JScrollPane(displayArea), BorderLayout.CENTER);
		displayArea.setEditable(false);
	}

	private void initMessageField() {
		messageField = new JTextField();
		messageField.setEditable(false);
		messageField.setFont(new Font(null, Font.BOLD, 15));
		messageField.addActionListener(new ActionListener() {
			// send message to chosen client
			public void actionPerformed(ActionEvent event) {
				String melding = event.getActionCommand();
				if (chosenClient != null) {
					chosenClient.sendData(melding);
					displayMessage("-> " + chosenClient.toString() + ": "
							+ melding);
				}
			}
		});
		add(messageField, BorderLayout.NORTH);
	}

	@Override
	public void valueChanged(ListSelectionEvent lse) {
		chosenClient = (ClientConnection) clientModel.get(clientList
				.getSelectedIndex());
	}

	private void start() {
		try (ServerSocket serverSocket = new ServerSocket(8000, 100);) {
			while (true) {
				displayMessage("Waiting for request");
				Socket socket = serverSocket.accept();
				messageField.setEditable(true);
				displayMessage("Request " + ++numberOfClients
						+ " received from: "
						+ socket.getInetAddress().getHostName());
				ClientConnection clientConnection = new ClientConnection(
						socket, ++id);
				clientModel.addElement(clientConnection);
				numberOfClientsLabel.setText("Number of clients connected: "
						+ numberOfClients);
			}
		} catch (IOException e) {
			displayMessage(e.getMessage());
		}
	}

	private class ClientConnection implements Runnable {
		private Socket clientContact;
		private ObjectOutputStream output;
		private ObjectInputStream input;
		private int id;

		public ClientConnection(Socket clientContact, int id) {
			this.id = id;
			this.clientContact = clientContact;
			new Thread(this).start();
		}

		@Override
		public void run() {
			try {
				handleClientCommunication();
			} catch (ClassNotFoundException e) {
				displayMessage(e.getMessage());
			} catch (IOException e) {
				displayMessage(e.getMessage());
			} finally {
				try {
					output.close();
					input.close();
					clientContact.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void handleClientCommunication() throws IOException,
				ClassNotFoundException {
			output = new ObjectOutputStream(clientContact.getOutputStream());
			output.flush();
			input = new ObjectInputStream(clientContact.getInputStream());
			String message = "You have ID: " + id;
			sendData(message);
			do {
				message = (String) input.readObject();
				displayMessage("Client " + id + " -> " + message);

			} while (!("end".equals(message)));
			removeClient();
		}

		private void removeClient() {
			displayMessage("Closing connection with client " + id + "\n");
			numberOfClients--;
			numberOfClientsLabel.setText("Number of clients connected: "
					+ numberOfClients);
			clientModel.removeElement(this);
			if (numberOfClients == 0)
				messageField.setEditable(false);
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

		public void sendData(String message) {
			try {
				output.writeObject("Server -> " + message);
				output.flush();
			} catch (IOException e) {
				displayMessage(e.getMessage());
			}
		}

	}

	private void displayMessage(String message) {
		displayArea.append(message + "\n");
		displayArea.setCaretPosition(displayArea.getText().length());
	}

	public static void main(String args[]) {
		new ChatServer1();
	}
}