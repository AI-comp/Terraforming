package net.aicomp.terraforming;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class MainTest {
	@Before
	public void before() throws IOException, InterruptedException {
		System.out.println("Start compiling.");
		ProcessBuilder pb = new ProcessBuilder("SampleAI/build.bat");
		pb.directory(new File("SampleAI"));
		Process p = pb.start();
		p.waitFor();
		System.out.println("Finish compiling.");
	}

	@Test
	public void runThreeSampleJavaAIPrograms() {
		String command = "java -cp SampleAI/Java Main";
		Main.main(new String[] { "-c", "-a", command, command, command });
	}

	@Test
	public void runThreeSampleCppAIPrograms() {
		String command = "SampleAI/Cpp/a.exe";
		Main.main(new String[] { "-c", "-a", command, command, command });
	}

	@Test
	public void runThreeSampleHaskellAIPrograms() {
		String command = "SampleAI/Haskell/Main.exe";
		Main.main(new String[] { "-c", "-a", command, command, command });
	}

	@Test
	public void runThreeSampleScalaAIPrograms() {
		String command = "scala.bat -cp SampleAI/Scala Main";
		Main.main(new String[] { "-c", "-a", command, command, command });
	}

	@Test
	public void runNonExistentJavaAIPrograms() {
		String command = "java -cp foo bar";
		Main.main(new String[] { "-c", "-a", command, command, command });
	}

	@Test
	public void runThreeHeavyAIPrograms() {
		String command = "java -cp SampleAI/HeavyJava Main";
		Main.main(new String[] { "-c", "-a", command, command, command });
	}
}
