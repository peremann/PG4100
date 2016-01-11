package no.westerdals.pg4100.threads1;

public class ConsiderateThread implements Runnable {
	private Thread t;
	
	//constructor
	public ConsiderateThread(String priority) {
		t = new Thread(this);
		t.setName(priority);
		if ("low".equals(priority)) t.setPriority(Thread.MIN_PRIORITY);
		else if ("medium".equals(priority)) t.setPriority(Thread.NORM_PRIORITY);
		else if ("maximum".equals(priority)) t.setPriority(Thread.MAX_PRIORITY);
		else t.setPriority(Thread.NORM_PRIORITY);
		t.start();
	}

	public void run() {
		for (int i = 1; i < 6; i++) {
			if (t.getName().equals("maximum")) {
				System.out.println(t.getName() + " with priority: "
						+ t.getPriority() + " calling yield()");
				Thread.yield();
			} else
				System.out.println( t.getName() + " with priority: " 
						+ t.getPriority());
		}
		System.out.println(t.getName() + " with priority: "
					+ t.getPriority() + " is done");
	}
}


