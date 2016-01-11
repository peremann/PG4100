package no.westerdals.pg4100.threads1;

public class TestPriority2 {
	public static void main(String[] args) {
		ConsiderateThread[] t = new ConsiderateThread[3];
		t[0] = new ConsiderateThread("low");
		t[1] = new ConsiderateThread("medium");
		t[2] = new ConsiderateThread("maximum");
		System.out.println("TestPriority2: Ending main()");
	}
}