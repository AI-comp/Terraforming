package net.aicomp.terraforming.manipulator

import java.util.Scanner
import net.aicomp.terraforming.entity.Game
import net.exkazuu.gameaiarena.manipulator.Manipulator
import net.exkazuu.gameaiarena.player.ExternalComputerPlayer
import net.aicomp.terraforming.scene.graphic.TextBoxScene

abstract class StartManipulator extends Manipulator[Game, Array[String], String] {
  protected var _name = ""
  protected var _game: Game = null

  override def getComputerPlayer = null
  override def runPreProcessing(game: Game) {
    _name = "nanashi"
    _game = game
  }
  override def runPostProcessing() = {
    Array(_name)
  }
}

class ConsoleUserStartManipulator(scanner: Scanner) extends StartManipulator {
  override def runProcessing() {
    _name = scanner.nextLine()
  }
}

class GraphicalUserStartManipulator() extends StartManipulator {
  override def runProcessing() {
    TextBoxScene.inputCommandLists() match {
      case Some(name) => _name = name
      case None => _name = null
    }
  }
}

class AIPlayerStartManipulator(playerId: Int, com: ExternalComputerPlayer) extends StartManipulator {
  override def runProcessing() {
    com.writeLine(_game.stringify(playerId))
    val line = com.readLine
    if (line != null) {
      val name = line.trim
      if (!name.isEmpty) {
        _name = name
      }
    }
  }
}
