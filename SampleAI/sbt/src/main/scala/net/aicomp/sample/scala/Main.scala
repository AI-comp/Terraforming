package net.aicomp.sample.scala

object Main {
  def main(args: Array[String]) {
    var line = ""
    System.out.println("SampleSbt")
    while ({ line = readLine(); line ne null }) {
      Iterator.continually(readLine()).takeWhile(_ != "EOS").toList
      System.out.println("finish")
    }
  }
}
