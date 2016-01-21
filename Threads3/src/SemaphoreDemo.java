import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This code is originally presented in Javaworld:
 * http://www.javaworld.com/article
 * /2078809/java-concurrency/java-101-the-next-generation
 * -java-concurrency-without-the-pain-part-1.html The code is edited to be used
 * as an exercise.
 *
 */
public class SemaphoreDemo {

	/**
	 * Let's try three times as many threads as items to establish the purpose
	 * of a semaphore.
	 */
	public static final int NUMBER_OF_THREADS = Pool.NUMER_OF_ITEMS * 5;

	public static void main(String[] args) {
		final Pool pool = new Pool();
		ExecutorService executor = Executors
				.newFixedThreadPool(NUMBER_OF_THREADS);
		DemoThread[] threads = new DemoThread[NUMBER_OF_THREADS];
		for (int i = 0; i < NUMBER_OF_THREADS; i++) {
			threads[i] = new DemoThread(pool);
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
	private int fetchCount = 0;

	public DemoThread(Pool pool) {
		this.pool = pool;
	}

	@Override
	public void run() {
		String name = Thread.currentThread().getName();
		try {
			while (true) {
				if (pool.stopFetching()) {
					return;
				}
				String item;
				Thread.sleep(100 + (int) (Math.random() * 100));
				System.out.printf("%s acquiring %s%n", name,
						item = pool.getItem());
				fetchCount++;
				Thread.sleep(100 + (int) (Math.random() * 100));
				System.out.printf("%s putting back %s%n", name, item);
				pool.putItem(item);
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
	Condition itemAvailable = lock.newCondition();

	Pool() {
		items = new String[NUMER_OF_ITEMS];
		for (int i = 0; i < items.length; i++)
			items[i] = "ITEM" + i;
	}

	String getItem() throws InterruptedException {
		lock.lock();
		try {
			String item = getNextAvailableItem();
			while (item == null) {
				itemAvailable.await();
				item = getNextAvailableItem();
			}
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
		markAsUnused(item);
		itemAvailable.signal();
		lock.unlock();
	}

	private String getNextAvailableItem() {
		for (int i = 0; i < NUMER_OF_ITEMS; ++i) {
			if (!used[i]) {
				used[i] = true;
				return items[i];
			}
		}
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