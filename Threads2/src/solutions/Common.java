package solutions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Common {
	private boolean updateMinute = false;
	private ReentrantLock lock;
	private Condition newMinute;

	public Common() {
		this.lock = new ReentrantLock();
		newMinute = lock.newCondition();
	}

	public void hold() {
		lock.lock();
		try {
			while (!updateMinute) {
				newMinute.await();
			}
			updateMinute = false;
		} catch (InterruptedException e) {
		} finally {
			lock.unlock();
		}
	}

	public void minuteTick() {
		lock.lock();
		try {
			updateMinute = true;
			newMinute.signal();
		} finally {
			lock.unlock();
		}
	}
}
