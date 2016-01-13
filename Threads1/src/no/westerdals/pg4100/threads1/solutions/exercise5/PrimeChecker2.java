package no.westerdals.pg4100.threads1.solutions.exercise5;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class PrimeChecker2 extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel northPanel;
	private JPanel southPanel;
	private JPanel centerPanel;
	private JButton checkButton, helpButton;
	private JTextField numberField;
	private JTextArea displayArea;
	@SuppressWarnings("unused")
	private Checker1 checker;

	// 123211177 is a prime
	public PrimeChecker2() {
		super("Tests if a number is a prime");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(3, 2, 10, 10));
		northPanel.add(new JLabel("Number to be checked:"));
		numberField = new JTextField();
		northPanel.add(numberField);
		add(northPanel, BorderLayout.NORTH);

		centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(1, 1, 10, 10));
		displayArea = new JTextArea();
		centerPanel.add(new JScrollPane(displayArea));
		add(centerPanel, BorderLayout.CENTER);

		southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(1, 2, 10, 10));
		checkButton = new JButton("Start check");
		checkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long number = Long.parseLong(numberField.getText());
				checker = new Checker1(number);
			}
		});
		southPanel.add(checkButton);
		helpButton = new JButton("Help");
		helpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"OMG!!! This was hard work.\n"
								+ " Sorry for taking so long.");
			}
		});
		southPanel.add(helpButton);
		add(southPanel, BorderLayout.SOUTH);

		setSize(600, 250);
		setVisible(true);
	}

	public static void main(String[] args) {
		new PrimeChecker2();
		System.out.println("PrimeChecker2: main() ending");
	}

	private class Checker1 implements Runnable {
		private long number;

		public Checker1(long number) {
			this.number = number;
			new Thread(this).start();
		}

		// Checks the number
		public void run() {
			long i = 2;
			for (; i < number; i++)
				if (number % i == 0)
					break;
			if (i == number) {
				displayArea.append("The number " + number + " is a prime\n");
			} else {
				displayArea.append("The number " + number
						+ " is not a prime\n");
			}
		}
	}
}
