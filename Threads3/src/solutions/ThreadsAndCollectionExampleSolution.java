package solutions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadsAndCollectionExampleSolution {
	static Set<Integer> integers = Collections
			.synchronizedSet(new HashSet<Integer>());
	
	public static void main(String[] args) {
		new ThreadsAndCollectionExampleSolution();
	}

	public ThreadsAndCollectionExampleSolution() {
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
			synchronized (integers) {
				Iterator<Integer> iterator = integers.iterator();
				while (iterator.hasNext()) {
					int i = iterator.next().intValue();
					if (i % 100 == 0) {
						iterator.remove();
						//integers.remove(i);
					}
				}
			}
		}

	}

	static class DevisibleBy99Printer implements Runnable {
		@Override
		public void run() {
			synchronized (integers) {
				Iterator<Integer> iterator = integers.iterator();
				while (iterator.hasNext()) {
					int i = iterator.next().intValue();
					if (i % 99 == 0) {
						System.out.println(i);
					}
				}
			}
		}

	}
}
