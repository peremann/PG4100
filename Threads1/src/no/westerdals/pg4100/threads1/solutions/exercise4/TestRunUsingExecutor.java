package no.westerdals.pg4100.threads1.solutions.exercise4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import no.westerdals.pg4100.threads1.RunnableExample;

public class TestRunUsingExecutor {
	public static void main(String[] args) {
		RunnableExample runnable = new RunnableExample();
		Thread thread = new Thread(runnable);
		System.out.println("TestRunUsingExecutor: Reading from object runnable.getI()= " + runnable.getI());
		
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(thread);
		
		// Wait...
		JOptionPane.showMessageDialog(null, "Wait...");
		System.out.println("TestRunUsingExecutor: Reading from object after run"
				+ " has terminated: runnable.getI()= " + runnable.getI());

		System.out.println("TestRunUsingExecutor: Trying to start the thread again...");
		runnable.setI(5);
		executor.execute(thread);
		System.out.println("TestRunUsingExecutor: Ending main()");
		
		executor.shutdown();
	}
}
