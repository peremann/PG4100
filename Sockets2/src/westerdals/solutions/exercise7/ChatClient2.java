package westerdals.solutions.exercise7;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.util.*;

public class ChatClient2 extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField messageField;
	private JTextArea displayArea;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String serverAddress;
	private String nick;
	private String selectedNickName;
	private JList<String> nickNames;

	/**
	 * Initializes Client and GUI elements.
	 * 
	 * @param host the host to connect to
	 */
	public ChatClient2(String host) {
		selectedNickName = null;
		initNickNameList();
		serverAddress = host;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initMessageField();
		initDisplayArea();
		setSize(400, 200);
		setVisible(true);
		startClient();
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
			public void actionPerformed(ActionEvent event) {
				String message = event.getActionCommand();
				if ("end".equals(message)) {
					sendData(message);
					exit();
				} else if (selectedNickName == null) {
					displayMessage("You have to choose a recipient first!");
				} else {
					sendData(message);
					messageField.setText("");
				}
			}
		});
		add(messageField, BorderLayout.NORTH);
	}

	private void initNickNameList() {
		nickNames = new JList<String>(new String[5]);
		nickNames.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					selectedNickName = (String) nickNames.getSelectedValue();
					sendData("new " + selectedNickName);
					setTitle(nick + " -> " + selectedNickName);
				}
			}
		});
		add(nickNames, BorderLayout.EAST);
	}

	private void startClient() {
		displayMessage("Attempting to connnect");
		try (Socket serverContact = new Socket(
				InetAddress.getByName(serverAddress), 8000)) {
			initializeServerContact(serverContact);
			while (true) {
				try {
					handleMessagesFromServer();
				} catch (ClassNotFoundException classNotFoundException) {
					displayMessage("Received unkown object type");
				}
			}
		} catch (EOFException eofException) {
			System.out.println("Server closed the connection");
		} catch (IOException ioException) {
			System.out.println("Problem with server connection");
		} finally {
			displayMessage("Closing connection");
			messageField.setEditable(false);
			try {
				output.close();
				input.close();
			} catch (IOException ioException) {
				System.out.println("Problem with server connection");
			}
			exit();
		}
	}

	private void handleMessagesFromServer() throws IOException,
			ClassNotFoundException {
		String message;
		Object receivedInputFromServer = input.readObject();
		System.out.println(receivedInputFromServer);
		if (inputIsClientNickNames(receivedInputFromServer)) {
			@SuppressWarnings("unchecked")
			Vector<String> vector = (Vector<String>) receivedInputFromServer;
			nickNames.setListData(vector);
		} else {
			message = (String) receivedInputFromServer;
			displayMessage(message);
			if (message.contains("is already logged in!"))
				exit();
		}
	}

	private boolean inputIsClientNickNames(Object input) {
		return input instanceof Vector<?>;
	}

	private void initializeServerContact(Socket serverContact) throws IOException {
		displayMessage("Connected to: "
				+ serverContact.getInetAddress().getHostName());
		output = new ObjectOutputStream(serverContact.getOutputStream());
		output.flush();
		input = new ObjectInputStream(serverContact.getInputStream());
		messageField.setEditable(true);
		nick = JOptionPane.showInputDialog("Enter nick");
		setTitle(nick);
		sendData(nick);
	}

	private void exit() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private void sendData(String melding) {
		try {
			output.writeObject(melding);
			output.flush();
			displayMessage(melding);
		} catch (IOException ioException) {
			displayArea.append("Exception when writing");
		}
	}

	private void displayMessage(String melding) {
		displayArea.append(melding + "\n");
		displayArea.setCaretPosition(displayArea.getText().length());
	}

	public static void main(String args[]) throws ClassNotFoundException {
		new ChatClient2("127.0.0.1");
	}

}