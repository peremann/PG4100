package no.westerdals.pg4100.threads1.solutions.exercise3;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Fills a portion of frame window with filled circles,
 * one at a time with a small pause between each.
 */
public class FillDemo2 extends JFrame implements ActionListener, Runnable {
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 300;
	public static final int HEIGHT = 200;
	public static final int FILL_WIDTH = 300;
	public static final int FILL_HEIGHT = 100;
	public static final int CIRCLE_SIZE = 10;
	public static final int PAUSE = 100; // milliseconds
	private JButton startButton;
	private JButton avsluttButton;

	private JPanel box;

	public FillDemo2() {
		setSize(WIDTH, HEIGHT);
		setTitle("FillDemo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new BorderLayout());

		box = new JPanel();
		add(box, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		startButton = new JButton("Start");
		startButton.addActionListener(this);
		buttonPanel.add(startButton);
		avsluttButton = new JButton("Avslutt");
		avsluttButton.addActionListener(this);
		buttonPanel.add(avsluttButton);
		add(buttonPanel, BorderLayout.SOUTH);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == avsluttButton)
			System.exit(0);
		else
			new Thread(this).start();
	}

	public void fill() {
		Graphics g = box.getGraphics();

		for (int y = 0; y < FILL_HEIGHT; y = y + CIRCLE_SIZE)
			for (int x = 0; x < FILL_WIDTH; x = x + CIRCLE_SIZE) {
				g.fillOval(x, y, CIRCLE_SIZE, CIRCLE_SIZE);
				doNothing(PAUSE);
			}
	}

	public void doNothing(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			System.out.println("Unexpected interrupt");
			System.exit(0);
		}
	}

	@Override
	public void run() {
		fill();
	}
}
