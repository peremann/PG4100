package westerdals.solutions.exercise4;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChatClient1 extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField messageField;
	private JTextArea displayArea;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String serverAddress;

	public ChatClient1(String host) {
		super("Client");
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
				sendData(event.getActionCommand());
				messageField.setText("");
			}
		});
		messageField.setEditable(true);
		add(messageField, BorderLayout.NORTH);
	}

	private void startClient() {
		String message = "";
		try (Socket serverContact = new Socket(
				InetAddress.getByName(serverAddress), 8000)) {
			initServerContact(serverContact);
			do {
				message = readMessageFromServer(message);
			} while (!(message.equals("end")));
		} catch (EOFException eofException) {
			System.out.println("Server closed the connection");
		} catch (IOException ioException) {
			System.out.println("Problem with server connection");
		} finally {
			exit();
		}
	}

	private void exit() {
		displayMessage("Closing connection");
		sendData("end");
		messageField.setEditable(false);
		try {
			output.close();
			input.close();
		} catch (IOException ioException) {
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private String readMessageFromServer(String message) throws IOException {
		try {
			message = (String) input.readObject();
			displayMessage(message);
		} catch (ClassNotFoundException classNotFoundException) {
			displayMessage("Received unknown object type");
		}
		return message;
	}

	private void initServerContact(Socket serverContact) throws IOException {
		displayMessage("Trying to connect");
		displayMessage("Connected: "
				+ serverContact.getInetAddress().getHostName());
		output = new ObjectOutputStream(serverContact.getOutputStream());
		output.flush();
		input = new ObjectInputStream(serverContact.getInputStream());
	}

	private void sendData(String message) {
		try {
			output.writeObject(message);
			output.flush();
			displayMessage(message);
		} catch (IOException ioException) {
			displayArea.append("Exception when sending");
		}
	}

	private void displayMessage(String message) {
		displayArea.append(message + "\n");
		displayArea.setCaretPosition(displayArea.getText().length());
	}

	public static void main(String args[]) {
		new ChatClient1("127.0.0.1");
	}

}