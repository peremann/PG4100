package solutions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The example is from the Oracle doc:
 * http://docs.oracle.com/javase/tutorial/essential/concurrency/deadlock.html
 * The exampl eis rewritten to use locks.
 * @author lauper
 *
 */
public class Deadlock {
	static class Friend {
		private final String name;

		private static ReentrantLock lock = new ReentrantLock();
		private Condition bowedBack = lock.newCondition();
		
		public Friend(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public void bow(Friend bower) {
			lock.lock();
			System.out.format("Me, %s, is bowing to %s%n", this.name,
					bower.getName());
			try {
				bowedBack.await();
			} catch (InterruptedException e) {
				return;
			}
			bower.bowBack(this);
			lock.unlock();
		}

		public void bowBack(Friend bower) {
			lock.lock();
			System.out.format("%s: %s" + " has bowed back to me!%n", this.name,
					bower.getName());
			bowedBack.signal();
			lock.unlock();
		}
	}

	public static void main(String[] args) {
		final Friend alphonse = new Friend("Alphonse");
		final Friend gaston = new Friend("Gaston");
		new Thread(new Runnable() {
			public void run() {
				alphonse.bow(gaston);
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				gaston.bow(alphonse);
			}
		}).start();
	}
}
