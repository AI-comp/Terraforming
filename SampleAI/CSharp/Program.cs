using System;

public class AI {
  public static void Main(string[] args) {
    Console.WriteLine("SampleCSharp");
    while (true) {
      var line = Console.ReadLine();
      if (line == null) break;
      if (line != "EOS") continue;
      Console.WriteLine("finish");
    }
  }
}

