package net.aicomp.entity.input

import java.util.Scanner

import scala.collection.mutable.ListBuffer

import net.aicomp.entity.Game
import net.aicomp.scene.graphic.TextBoxScene
import net.exkazuu.gameaiarena.player.ExternalComputerPlayer

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

class ExternalProgramInput(commands: Array[String]) extends ExternalComputerPlayer(commands) with Input {
  def inputCommandLists(game: Game) = {
    if (game != null) {
      super.writeLine(game.stringify)
    }
    val buf = ListBuffer[String]()
    // TODO: Rewrite the following code to use more functional features
    var line = ""
    while (line.trim != "finish") {
      line = super.readLine
      buf += line
    }
    buf.map(_.split(" ").toArray).toArray
  }
}
