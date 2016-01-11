package no.westerdals.pg4100.threads1;

public class SmallJob1MainUsingThreads {
	public static void main(String[] args) {
		// Testing SmallJob1AsThread
		System.out.println("SmallJob1Test: main() is starting .....");
		SmallJob1AsThread job1 = new SmallJob1AsThread("Job 1");
		SmallJob1AsThread job2 = new SmallJob1AsThread("Job 2");
		SmallJob1AsThread job3 = new SmallJob1AsThread("Job 3");
		new Thread(job1).start();
		new Thread(job2).start();
		new Thread(job3).start();
		System.out.println("SmallJob1Test: main() is done .....");

		/*
		 * // Testing SmallJob1AsThreadSubclass
		 * System.out.println("SmallJob1Test: main() starter .....");
		 * SmallJob1AsThreadSubclass job1 = new
		 * SmallJob1AsThreadSubclass("Job 1"); SmallJob1AsThreadSubclass job2 =
		 * new SmallJob1AsThreadSubclass("Job 2"); SmallJob1AsThreadSubclass
		 * job3 = new SmallJob1AsThreadSubclass("Job 3"); job1.start();
		 * job2.start(); job3.start();
		 * System.out.println("SmallJob1Test: main() avslutter .....");
		 */
	}
}
