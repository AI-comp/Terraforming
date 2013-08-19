package net.aicomp.terraforming.manipulator

import java.util.Scanner

import scala.collection.mutable.Queue

import net.aicomp.terraforming.entity.Game
import net.aicomp.terraforming.entity.Player
import net.aicomp.terraforming.scene.graphic.TextBoxUtils
import net.exkazuu.gameaiarena.manipulator.Manipulator
import net.exkazuu.gameaiarena.player.ExternalComputerPlayer

abstract class GameManipulator extends Manipulator[Game, Array[String], String] {
  override def getComputerPlayer = null
}

class NopGameManipulator extends GameManipulator {
  override def runPreProcessing(game: Game) {}
  override def runProcessing() {}
  override def runPostProcessing() = Array()
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
    TextBoxUtils.inputCommandLists() match {
      case Some(cmd) => _commandString = cmd
      case None => _commandString = null
    }
  }
  override def runPostProcessing() = {
    Array(_commandString)
  }
}

class AIPlayerGameManipulator(player: Player, com: ExternalComputerPlayer) extends GameManipulator {
  private var _commands: Queue[String] = Queue()
  private var _game: Game = null

  override def runPreProcessing(game: Game) {
    _commands = Queue()
    _game = game
  }

  override def runProcessing() {
    com.writeLine(_game.stringify(player))
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
    val commands = _commands.takeWhile(_ != "finish")
    commands += "finish"
    commands.toArray
  }

  private def isFinished() = _commands.lastOption.exists(_ == "finish")
}

class InternalAIPlayerGameManipulator(player: Player, manipulator: InternalManipulator) extends GameManipulator {
  protected var _commands: Queue[String] = Queue()
  protected var _game: Game = null

  final override def runPreProcessing(game: Game) {
    _commands = Queue()
    _game = game
  }

  final override def runProcessing() {
    val cmds = manipulator.run(_game.stringify(player), _game, player)
    val array = if (cmds != null) cmds.toArray(Array[String]()) else Array[String]()
    _commands = Queue(array: _*)
  }

  final override def runPostProcessing() = {
    val commands = _commands.takeWhile(_ != "finish")
    commands += "finish"
    commands.toArray
  }
}
