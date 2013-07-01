package net.aicomp.manipulator

import java.util.Scanner

import net.aicomp.entity.Game
import net.aicomp.scene.graphic.TextBoxScene
import net.exkazuu.gameaiarena.player.ExternalComputerPlayer
import net.exkazuu.gameaiarena.runner.AbstractRunner

abstract class StartManipulator extends AbstractRunner[Game, String, String] {
  protected var _name = ""
  protected var _game: Game = null

  override def getComputerPlayer = null
  override def runPreProcessing(game: Game) {
    _name = "nanashi"
    _game = game
  }
  override def runPostProcessing() = {
    _name
  }
}

class ConsoleUserStartManipulator(scanner: Scanner) extends StartManipulator {
  override def runProcessing() {
    do {
      Thread.sleep(10)
      val name = scanner.nextLine()
      if (name != null) {
        _name = name.trim
      }
    } while (_name == "")
  }
}

class GraphicalUserStartManipulator() extends StartManipulator {
  override def runProcessing() {
    do {
      Thread.sleep(10)
      TextBoxScene.inputCommandLists() match {
        case Some(name) => _name = name.trim
        case None => _name = ""
      }
    } while (_name == "")
  }
}

class AIPlayerStartManipulator(playerId: Int, com: ExternalComputerPlayer) extends StartManipulator {
  override def runProcessing() {
    com.writeLine(_game.stringify(playerId))
    val line = com.readLine
    if (line != null) {
      val name = line.trim
      if (name != "") {
        _name = name
      }
    }
  }
}
