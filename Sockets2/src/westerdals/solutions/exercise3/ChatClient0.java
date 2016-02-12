package westerdals.solutions.exercise3;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChatClient0 extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField messageField;
	private JTextArea displayArea;
	private String serverAddress;
	private ObjectOutputStream output;
	private ObjectInputStream input;

	/**
	 * Initializes Client and GUI elements.
	 * @param host the server host to communicate with
	 */
	public ChatClient0(String host) {
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

	/**
	 * Initialize the message field sending messages to server.
	 */
	private void initMessageField() {
		messageField = new JTextField();
		messageField.setEditable(true);
		messageField.setFont(new Font(null, Font.BOLD, 15));
		messageField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sendData(event.getActionCommand());
				messageField.setText("");
			}
		});
		add(messageField, BorderLayout.NORTH);
	}

	/**
	 * Create the socket and read messages from Server.
	 */
	private void startClient() {
		try (Socket serverContact = new Socket(serverAddress, 8000)){
			output = new ObjectOutputStream(serverContact.getOutputStream());
			output.flush();
			input = new ObjectInputStream(serverContact.getInputStream());
			String melding;
			while (true) {
				try {
					melding = (String) input.readObject();
					displayMessage(melding);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			exit();
		}

	}

	private void exit() {
		try {
			output.close();
			input.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		displayMessage("Closing");
		try {
			Thread.sleep(5000);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void displayMessage(String melding) {
		displayArea.append(melding + "\n");
		displayArea.setCaretPosition(displayArea.getText().length());
	}

	public static void main(String args[]) {
		String ipAdresse = "localhost";
		new ChatClient0(ipAdresse);
	}

}