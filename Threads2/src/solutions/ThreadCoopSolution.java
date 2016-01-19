package solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class ThreadCoopSolution {
	private Account account = new Account();
	public static final boolean testingFairness = false;

	public static void main(String[] args) {
		new ThreadCoopSolution();
	}

	public ThreadCoopSolution() {
		ExecutorService executor = Executors.newCachedThreadPool();
		List<WithdrawTask> withdrawTasks = getWithdrawTasks(4);
		DepositTask depositTask = new DepositTask();
		executor.execute(depositTask);
		for (WithdrawTask withdrawTask : withdrawTasks) {
			executor.execute(withdrawTask);
		}
		sleep(10);
		System.out.println("Shutting down executor");
		executor.shutdownNow();
		printResults(depositTask, withdrawTasks);
	}

	private void printResults(DepositTask depositTask, List<WithdrawTask> withdrawTasks) {
		int totalWithdrawals = 0;
		for (WithdrawTask withdrawTask : withdrawTasks) {
			System.out.println(withdrawTask.getName() + " withdrew: "
					+ withdrawTask.getWithrawn());
			totalWithdrawals += withdrawTask.getWithrawn();
		}
		System.out.println("Total: " + totalWithdrawals);
		System.out.println("Deposits: " + depositTask.totalDeposits);
		System.out.println("Current balance: " + account.balance);
		System.out
				.println("Diff: "
						+ (depositTask.totalDeposits - account.balance - totalWithdrawals));
	}

	private void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private List<WithdrawTask> getWithdrawTasks(
			int numberOfWithdrawTasks) {
		List<WithdrawTask> withdrawTasks = new ArrayList<>();
		for (int i = 0; i < numberOfWithdrawTasks; i++) {
			withdrawTasks.add(new WithdrawTask("w" + i));
		}
		return withdrawTasks;
	}

	private class DepositTask implements Runnable {
		int totalDeposits = 0;

		@Override
		// Keep adding an amount to the account
		public void run() {
			try { // Purposely delay it to let the withdraw method proceed
				while (true) {
					/*
					 * We need this in case the thread is interrupted in Ready or Running state.
					 */
					if(Thread.interrupted()){
						System.out.println("Wow, it hit me! Deposit.interrupted()");
						throw new InterruptedException();
					}
					int amount = (int) (Math.random() * 10) + 1;
					account.deposit(amount);
					totalDeposits += amount;
					Thread.sleep(1000);
				}
			} catch (InterruptedException ex) {
				System.out.println("In deposit interrupt-catch. I return...");
			}
		}
	}

	private class WithdrawTask implements Runnable {
		int withrawn;
		String name;

		public WithdrawTask(String name) {
			this.withrawn = 0;
			this.name = name;
		}

		@Override
		// Keep subtracting an amount from the account
		public void run() {
			try{
				while (true) {
					if (Thread.interrupted()){
						/*
						 * We need this in case the thread is interrupted in Ready or Running state.
						 */
						System.out.println("Wow, it hit me! Withdraw.interrupted()");
						return;
					}
					/*
					 * While testing fairness, all threads should withdraw the same amount.
					 */
					int amount = getWithdrawAmount();
					account.withdraw(amount);
					withrawn = withrawn + amount;
				}
			}catch (InterruptedException e) {
				System.out.println("In withdraw interrupt-catch. I return...");
				return;
			}
		}
		
		private int getWithdrawAmount() {
			if(testingFairness){
				return 3;
			}
			return (int) (Math.random() * 10) + 1;
		}

		public int getWithrawn() {
			return withrawn;
		}

		public String getName() {
			return name;
		}

	}

	// An inner class for account
	private class Account {
		// Create a new lock
		private Lock lock = new ReentrantLock(testingFairness);

		// Create a condition
		private Condition newDeposit = lock.newCondition();

		private int balance = 0;

		public int getBalance() {
			return balance;
		}

		public void withdraw(int amount) throws InterruptedException {
			lock.lock(); // Acquire the lock
			try {
				while (balance < amount) {
					System.out.println("\t\t\tWait for a deposit");
					newDeposit.await();
				}

				balance -= amount;
				System.out.println("\t\t\tWithdraw " + amount + "\t\t"
						+ getBalance());
			} finally {
				lock.unlock(); // Release the lock
			}
		}

		public void deposit(int amount) {
			lock.lock(); // Acquire the lock
			try {
				balance += amount;
				System.out.println("Deposit " + amount + "\t\t\t\t\t"
						+ getBalance());

				// Signal thread waiting on the condition
				newDeposit.signalAll();
			} finally {
				lock.unlock(); // Release the lock
			}
		}
	}
}
