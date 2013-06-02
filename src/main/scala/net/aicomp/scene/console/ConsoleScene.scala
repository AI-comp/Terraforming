package net.aicomp.scene.console

import java.util.Scanner
import net.aicomp.scene.CommandBaseScene

trait ConsoleScene extends CommandBaseScene {
  override def nextCommand = ConsoleScene.nextCommand

  override def displayCore(text: String) = print(text)
}

object ConsoleScene {
  var scanner = new Scanner(System.in)

  def nextCommand = {
    Some(scanner.nextLine().split(" ").filter(_.length > 0).toList)
  }
}