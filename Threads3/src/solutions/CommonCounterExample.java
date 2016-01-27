package solutions;

/**
 * 
 * @author lauper
 *
 */
public class CommonCounterExample {
	public static void main(String... args) {
		new CommonCounterExample().runExample();
	}

	public void runExample() {
		Counter counter = new Counter();
		Thread t1 = new Thread(new IncrementCounter(counter));
		Thread t2 = new Thread(new IncrementCounter(counter));
		t1.start();
		t2.start();
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Expecting value 2000000. Getting: " + counter.counter);
	}

	private class Counter {
		// public volatile long counter = 0; //Hmmm. I can see no difference
		// using volatile like this...
		public long counter = 0;
	}

	private class IncrementCounter implements Runnable {
		Counter counter;

		public IncrementCounter(Counter counter) {
			this.counter = counter;
		}

		@Override
		public void run() {
			for (int i = 0; i < 1000000; i++) {
				counter.counter++;
//				synchronized(counter){// this will provide the expected result.
//					counter.counter++;
//				}
			}
	}

}}
