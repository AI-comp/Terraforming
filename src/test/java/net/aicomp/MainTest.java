package net.aicomp;

import org.junit.Test;

public class MainTest {
	@Test
	public void runThreeSimpleJavaAIPrograms() {
		String command = "java -cp SampleAI SimpleJavaAI";
		Main.main(new String[] { "-c", "-a", command, command, command });
	}

	@Test
	public void runThreeSimpleScalaAIPrograms() {
		String command = "java -cp SampleAI SimpleScalaAI";
		Main.main(new String[] { "-c", "-a", command, command, command });
	}

	@Test
	public void runNonExistentJavaAIPrograms() {
		String command = "java -cp foo bar";
		Main.main(new String[] { "-c", "-a", command, command, command });
	}

	@Test
	public void runThreeHeavyAIPrograms() {
		String command = "java -cp SampleAI HeavyJavaAI";
		Main.main(new String[] { "-c", "-a", command, command, command });
	}
}
