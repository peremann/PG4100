package no.westerdals.pg4100.threads1;

public class TestPriority1 {
	public static void main(String[] args) {
		SelfishThread[] t = new SelfishThread[3];
		t[0] = new SelfishThread("low");
		t[1] = new SelfishThread("medium");
		t[2] = new SelfishThread("maximum");
		System.out.println("TestPriority1: Ending main()");
	}
}