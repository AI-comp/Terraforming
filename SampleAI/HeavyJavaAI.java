import java.util.Scanner;

public class HeavyJavaAI {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		boolean first = true;
		while (scanner.hasNext()) {
			while (!scanner.nextLine().equals("EOS")) {
				// do nothing
			}
			if (first) {
				first = false;
				System.out.println("SampleJavaAI");
			}
			else {
				System.out.println("finish");
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
		}
		scanner.close();
	}
}
