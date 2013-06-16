package net.aicomp;

import org.junit.Test;

public class MainTest {
	@Test
	public void runThreeAIPrograms() {
		String command = "java -cp SampleAI SimpleJavaAI";
		Main.main(new String[] { "-c", "-a", command, command, command });
	}
}
