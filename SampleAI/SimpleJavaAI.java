import java.util.Scanner;

public class SimpleJavaAI {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		boolean first = true;
		System.out.println("Java");
		while (scanner.hasNext()) {
			while (!scanner.nextLine().equals("EOS")) {
				// do nothing
			}
			System.out.println("finish");
		}
		scanner.close();
	}
}
