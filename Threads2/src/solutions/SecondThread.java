package solutions;

import javax.swing.JTextField;

public class SecondThread implements Runnable {
	private int seconds;
	private Common common;
	private JTextField textField;

	public SecondThread(Common c, JTextField f) {
		common = c;
		textField = f;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {}
			if (seconds == 59) {
				common.minuteTick();
				seconds = 0;
			}
			else
				seconds++;
			String text = "";
			if (seconds < 10)
				text += "0";
			text += seconds;
			textField.setText(text);
		}
	}
}
