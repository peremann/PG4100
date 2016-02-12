package westerdals.solutions.exercise2;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SimpleClient {

	public SimpleClient() {
		startKlient();
	}

	private void startKlient() {
		// Step 1 - creating Socket for server connection
		System.out.println("Trying to connect");
		try (Socket serverkontakt = new Socket("127.0.0.1", 8000);
		// Step 2 - obtaining input/output streams
				ObjectOutputStream output = new ObjectOutputStream(
						serverkontakt.getOutputStream())) {
			output.flush();
			System.out.println("Connected");
			sendObject(output);
		} catch (EOFException eofException) {
			System.out.println("Server closing connection\n");
		} catch (IOException ioException) {
			System.out.println("Problem with server connection\n");
		}
	}

	private void sendObject(ObjectOutputStream output) throws IOException {
		PersonEdited person = new PersonEdited("Per", 41);
		output.writeObject(person);
		output.flush();
	}

	public static void main(String args[]) {
		new SimpleClient().startKlient();
	}
} // end class SimpleClient