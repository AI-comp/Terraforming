package net.aicomp.sample.maven;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("SampleMaven");
    while (scanner.hasNext()) {
      while (!scanner.nextLine().equals("EOS")) {
        // do nothing
      }
      System.out.println("finish");
    }
    scanner.close();
  }
}
