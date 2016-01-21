import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 
 * @author lauper
 *
 */
public class FutureExample {
	public static void main(String... args) {
		try {
			new FutureExample().runExample();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void runExample() throws InterruptedException, ExecutionException {
		ExecutorService executor = Executors.newCachedThreadPool();
		System.out.println("Submitting callable...");
		//Below is an anonymous class. We will later see how we can use lambdas...
		Future<Boolean> result = executor.submit(new Callable<Boolean>() {
			//Hint: 123211177 is a prime. You can use it as input...
			@Override
			public Boolean call() throws Exception {
				try (Scanner scanner = new Scanner(System.in)) {
					System.out.println("Enter a large number:");
					long input = scanner.nextLong();
					long i = 2;
					for (; i < input; i++)
						if (input % i == 0) 
							break;
					return (i == input);
				}
			}

		});
		executor.shutdown();
		System.out.println("Entered number is prime:" + result.get());
	}
}
