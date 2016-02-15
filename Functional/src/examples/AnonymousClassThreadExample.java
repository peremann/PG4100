package examples;

public class AnonymousClassThreadExample {

	public static void main(String... args) {
		new AnonymousClassThreadExample().runExample();
	}

	private void runExample() {
		Runnable run = new Runnable() {
			public void run() {
				System.out.println("hello world");
			}
		};
		new Thread(run).start();
	}
}
