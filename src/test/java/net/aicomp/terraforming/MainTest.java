package net.aicomp.terraforming;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class MainTest {
	@Before
	public void before() throws IOException, InterruptedException {
		System.out.println("Start compiling.");
		ProcessBuilder pb = new ProcessBuilder("build.bat");
		pb.directory(new File("SampleAI"));
		Process p = pb.start();
		p.waitFor();
		System.out.println("Finish compiling.");
	}

	@Test
	public void runThreeSimpleJavaAIPrograms() {
		String command = "java -cp SampleAI SimpleJavaAI";
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

	@Test
	public void runThreeSampleScalaAIPrograms() {
		String command = "java -cp SampleAI SampleScalaAI";
		Main.main(new String[] { "-c", "-a", command, command, command });
	}

	@Test
	public void runThreeSampleCppAIPrograms() {
		String command = "a.exe";
		Main.main(new String[] { "-c", "-a", command, command, command });
	}
}
