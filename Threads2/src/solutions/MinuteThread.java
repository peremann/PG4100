package solutions;

import javax.swing.*;

public class MinuteThread implements Runnable {

	private int minutes;
	private Common common;
	private JTextField textField;

	public MinuteThread(Common c, JTextField t) {
		common = c;
		textField = t;
	}

	public void run() {
		while (true) {
			common.hold();
			if (minutes == 59)
				minutes = 0;
			else
				minutes++;
			String text = "";
			if (minutes < 10)
				text += "0";
			text += minutes;
			textField.setText(text);
		}
	}
}
