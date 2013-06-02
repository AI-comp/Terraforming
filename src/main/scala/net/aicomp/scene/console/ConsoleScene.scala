package net.aicomp.scene.console

import java.util.Scanner
import net.aicomp.scene.AbstractScene

trait ConsoleScene extends AbstractScene {
  override def nextCommand = ConsoleScene.nextCommand

  override def displayCore(text: String) = print(text)
}

object ConsoleScene {
  var scanner = new Scanner(System.in)

  def nextCommand = {
    Some(scanner.nextLine().split(" ").filter(_.length > 0).toList)
  }
}