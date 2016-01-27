package solutions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This code is originally presented in Javaworld:
 * http://www.javaworld.com/article
 * /2078809/java-concurrency/java-101-the-next-generation
 * -java-concurrency-without-the-pain-part-1.html The code is edited to be used
 * as an exercise.
 *
 */
public class SemaphoreDemoSolution {

	/**
	 * Let's try three times as many threads as items to establish the purpose
	 * of a semaphore.
	 */
	public static final int NUMBER_OF_THREADS = Pool.NUMER_OF_ITEMS * 3;

	public static void main(String[] args) {
		final Pool pool = new Pool();
		/*
		 * The number of permits in the semaphore should equal the number of
		 * items in the pool.
		 */
		Semaphore available = new Semaphore(Pool.NUMER_OF_ITEMS, true);
		ExecutorService executor = Executors
				.newFixedThreadPool(NUMBER_OF_THREADS);
		DemoThread[] threads = new DemoThread[NUMBER_OF_THREADS];
		for (int i = 0; i < NUMBER_OF_THREADS; i++) {
			threads[i] = new DemoThread(pool, available);
			executor.execute(threads[i]);
		}
		executor.shutdown();
		try {
			executor.awaitTermination(60, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < threads.length; i++) {
			System.out.println("Thread " + i + ": fetches:"
					+ threads[i].getFetchCount());
		}
	}
}

class DemoThread implements Runnable {

	private Pool pool;
	private Semaphore available;
	private int fetchCount = 0;

	public DemoThread(Pool pool, Semaphore available) {
		this.pool = pool;
		this.available = available;
	}

	@Override
	public void run() {
		String name = Thread.currentThread().getName();
		try {
			while (true) {
				if (pool.stopFetching()) {
					return;
				}

				available.acquire();
				System.out.println(name + " has acquired permit for semaphore");
				try {
					String item;
					Thread.sleep(100 + (int) (Math.random() * 100));
					System.out.printf("%s acquiring %s%n", name,
							item = pool.getItem());
					fetchCount++;
					Thread.sleep(100 + (int) (Math.random() * 100));
					System.out.printf("%s putting back %s%n", name, item);
					pool.putItem(item);
				} finally {
					System.out.println(name + " releases permit for semaphore");
					available.release();
				}
			}
		} catch (InterruptedException ie) {
			System.out.printf("%s interrupted%n", name);
		}
	}

	public int getFetchCount() {
		return fetchCount;
	}

}

class Pool {
	public static final int NUMER_OF_ITEMS = 3;
	private int totalRemainingFetches = 10;
	private String[] items;
	private boolean[] used = new boolean[NUMER_OF_ITEMS];
	private ReentrantLock lock = new ReentrantLock();

	Pool() {
		items = new String[NUMER_OF_ITEMS];
		for (int i = 0; i < items.length; i++)
			items[i] = "ITEM" + i;
	}

	String getItem() throws InterruptedException {
		lock.lock();
		try {
			String item = getNextAvailableItem();
			/*
			 * Thre will be available items as we have
			 * limited the number of threads to access the items.
			 */
			totalRemainingFetches--;
			return item;
		} finally {
			lock.unlock();
		}
	}

	public boolean stopFetching() {
		return totalRemainingFetches <= 0;
	}

	void putItem(String item) {
		lock.lock();
		try {
			markAsUnused(item);
		} finally {
			lock.unlock();
		}
	}

	private String getNextAvailableItem() {
		for (int i = 0; i < NUMER_OF_ITEMS; ++i) {
			if (!used[i]) {
				used[i] = true;
				return items[i];
			}
		}
		/*
		 * This will not be reached when we are limiting the number of threads
		 * to be the same as the number of items.
		 */
		return null;
	}

	private boolean markAsUnused(String item) {
		for (int i = 0; i < NUMER_OF_ITEMS; ++i) {
			if (item == items[i]) {
				if (used[i]) {
					used[i] = false;
					return true;
				} else
					return false;
			}
		}
		return false;
	}
}