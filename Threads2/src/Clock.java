import javax.swing.*;
import java.awt.*;

class Clock extends JFrame {
	private static final long serialVersionUID = 1L;

	public Clock() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		JTextField seconds = new JTextField(2);
		JTextField minutes = new JTextField(2);
		seconds.setEditable(false);
		minutes.setEditable(false);
		add(minutes);
		add(seconds);
		setSize(200, 100);
		setVisible(true);
		/* Commented to avoid compilation errors.
		 * Remove comment and implement Common, MinuteThread and SecondThread 
		
		Common common = new Common();
		MinuteThread minute = new MinuteThread(common, minutes);
		SecondThread second = new SecondThread(common, seconds);
		*/
		// Start your threads (if they are not started).
	}

	public static void main(String[] args) {
		new Clock();
	}
}
