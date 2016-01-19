package solutions;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        Common common = new Common();
        MinuteThread minute = new MinuteThread(common, minutes);
        SecondThread second = new SecondThread(common, seconds);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(minute);
        executor.execute(second);
        executor.shutdown();
    }
    
    public static void main(String [] args) {
        new Clock();
    }
}
