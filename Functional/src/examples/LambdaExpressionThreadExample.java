package examples;

public class LambdaExpressionThreadExample {

	public static void main(String... args) {
		new LambdaExpressionThreadExample().runExample();
	}

	private void runExample() {
		new Thread(() -> System.out.println("hello world")).start();
	}
}
