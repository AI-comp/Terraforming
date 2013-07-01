package net.aicomp.manipulator

import net.exkazuu.gameaiarena.player.ExternalComputerPlayer
import java.util.Scanner
import net.aicomp.scene.graphic.TextBoxScene
import net.exkazuu.gameaiarena.runner.AbstractRunner
import net.aicomp.entity.Game
import scala.collection.mutable.Queue

abstract class GameManipulator extends AbstractRunner[Game, Array[String], String] {
  protected var _commands: Queue[String] = Queue()
  protected var _game: Game = null
  override def getComputerPlayer = null
  override def runPreProcessing(game: Game) {
    _commands = Queue()
    _game = game
  }
  override def runPostProcessing() = {
    val commands = _commands.takeWhile(_ == "finish")
    commands += "finish"
    commands.toArray
  }
  protected def isFinished() = _commands.lastOption.exists(_ == "finish")
}

class ConsoleUserGameManipulator(scanner: Scanner) extends GameManipulator {
  override def runProcessing() {
    def readLine() = {
      val line = scanner.nextLine()
      if (line == null) {
        "finish"
      } else {
        line.trim
      }
    }
    do {
      Thread.sleep(10)
      _commands.enqueue(readLine())
    } while (!isFinished)
  }
}

class GraphicalUserGameManipulator() extends GameManipulator {
  override def runProcessing() {
    do {
      Thread.sleep(10)
      TextBoxScene.inputCommandLists() match {
        case Some(cmd) => _commands.enqueue(cmd.trim)
        case None => ()
      }
    } while (!isFinished)
  }
}

class AIPlayerGameManipulator(playerId: Int, com: ExternalComputerPlayer) extends GameManipulator {
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
}
