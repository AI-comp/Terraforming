import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("HeavyJava");
		while (scanner.hasNext()) {
			while (!scanner.nextLine().equals("EOS")) {
				// do nothing
			}
			System.out.println("finish");
			try {
				Thread.sleep(4000000);
			} catch (InterruptedException e) {
			}
		}
		scanner.close();
	}
}
