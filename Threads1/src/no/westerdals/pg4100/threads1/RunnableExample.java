package no.westerdals.pg4100.threads1;

public class RunnableExample implements Runnable {
	private int i = 0;
	
	public RunnableExample() {
		System.out.println("RunnableExample: constructor ...");
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	@Override
	public void run() {
		System.out.println("RunnableExample: Starting run()... i = "
				+ i);
		i++;
		try {
			Thread.sleep(2000);
		} catch(InterruptedException iexc) {}
		
		System.out.println("RunnableExample: Finishing run()... i = "
				+ i);
	}
}