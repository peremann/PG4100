import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author lauper
 *
 */
public class ThreadsAndCollectionExample {
	static HashSet<Integer> integers = new HashSet<Integer>();

	public static void main(String[] args) {
		new ThreadsAndCollectionExample();
	}

	public ThreadsAndCollectionExample() {
		for (int i = 0; i < 1000; i++) {
			integers.add(new Integer(i));
		}
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(new DevisibleByHundredRemover());
		executor.execute(new DevisibleBy99Printer());
		executor.shutdown();
		try {
			executor.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Size after thread execution:" + integers.size());
	}

	static class DevisibleByHundredRemover implements Runnable {
		@Override
		public void run() {
			for (Integer integer : integers) {
				if (integer.intValue() % 100 == 0) {
					integers.remove(integer);
				}
			}

		}

	}

	static class DevisibleBy99Printer implements Runnable {
		@Override
		public void run() {
			for (Integer integer : integers) {
				if (integer.intValue() % 99 == 0) {
					System.out.println(integer);
				}
			}

		}

	}
}
