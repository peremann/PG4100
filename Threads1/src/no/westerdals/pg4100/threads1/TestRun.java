package no.westerdals.pg4100.threads1;

import javax.swing.JOptionPane;

public class TestRun {
	public static void main(String[] args) {
		RunnableExample runnable = new RunnableExample();
		Thread thread = new Thread(runnable);
		System.out.println("TestRun: Reading from object runnable.getI()= "
				+ runnable.getI());
		thread.start();
		
		// Wait...
		JOptionPane.showMessageDialog(null, "Wait...");
		System.out.println("TestRun: Reading from object after run"
				+ " has terminated: runnable.getI()= " + runnable.getI());
		
		System.out.println("TestRun: Trying to start the thread again......");
		runnable.setI(5);
		thread.start();
		System.out.println("TestRun: Ending main()");
	}
}