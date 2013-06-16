package net.aicomp.input

import java.util.Scanner

import net.aicomp.entity.Game
import net.aicomp.scene.graphic.TextBoxScene

trait Input {
  def inputCommandLists(game: Game): Array[Array[String]]
}

class ConsoleUserInput(scanner: Scanner) extends Input {
  def inputCommandLists(game: Game) = {
    Array(scanner.nextLine().split(" ").toArray)
  }
}

class GraphicalUserInput extends Input {
  def inputCommandLists(game: Game) = {
    TextBoxScene.inputCommandLists()
  }
}
