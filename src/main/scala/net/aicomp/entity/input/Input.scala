package net.aicomp.entity.input

import java.util.Scanner
import net.aicomp.entity.Game
import net.aicomp.scene.graphic.TextBoxScene
import net.exkazuu.gameaiarena.player.ExternalComputerPlayer
import net.exkazuu.gameaiarena.runner.AbstractRunner
import scala.collection.mutable.ListBuffer

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
    TextBoxScene.nextCommand()
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

class Manipulator(input: Input) extends AbstractRunner[Game, Array[Array[String]], Input] {
  private var _game: Game = null
  private var _commands: Array[Array[String]] = Array()

  override def getComputerPlayer = input

  override def runPreProcessing(game: Game) {
    _game = game
    _commands = Array()
  }

  override def runProcessing() {
    _commands = input.inputCommandLists(_game)
  }

  override def runPostProcessing() = _commands
}
