package net.aicomp.terraforming.manipulator

import java.util.Scanner
import scala.collection.mutable.Queue
import net.aicomp.terraforming.entity.Game
import net.exkazuu.gameaiarena.manipulator.Manipulator
import net.exkazuu.gameaiarena.player.ExternalComputerPlayer
import net.aicomp.terraforming.scene.graphic.TextBoxScene

abstract class GameManipulator extends Manipulator[Game, Array[String], String] {
  override def getComputerPlayer = null
}

class ConsoleUserGameManipulator(scanner: Scanner) extends GameManipulator {
  private var _commandString: String = null

  override def runPreProcessing(game: Game) {}
  override def runProcessing() {
    _commandString = scanner.nextLine()
  }
  override def runPostProcessing() = {
    Array(_commandString)
  }
}

class GraphicalUserGameManipulator() extends GameManipulator {
  private var _commandString: String = null

  override def runPreProcessing(game: Game) {}
  override def runProcessing() {
    TextBoxScene.inputCommandLists() match {
      case Some(cmd) => _commandString = cmd
      case None => _commandString = null
    }
  }
  override def runPostProcessing() = {
    Array(_commandString)
  }
}

class AIPlayerGameManipulator(playerId: Int, com: ExternalComputerPlayer) extends GameManipulator {
  protected var _commands: Queue[String] = Queue()
  protected var _game: Game = null

  override def runPreProcessing(game: Game) {
    _commands = Queue()
    _game = game
  }

  override def runProcessing() {
    com.writeLine(_game.stringify(playerId))
    def readLine() = {
      val line = com.readLine
      if (line == null) {
        "finish"
      } else {
        line.trim
      }
    }
    do {
      _commands.enqueue(readLine())
    } while (!isFinished)
  }

  override def runPostProcessing() = {
    val commands = _commands.takeWhile(_ == "finish")
    commands += "finish"
    commands.toArray
  }

  protected def isFinished() = _commands.lastOption.exists(_ == "finish")
}
