package net.aicomp.terraforming.manipulator

import java.util.Scanner

import net.aicomp.terraforming.entity.Game
import net.aicomp.terraforming.entity.Player
import net.aicomp.terraforming.scene.graphic.TextBoxScene
import net.exkazuu.gameaiarena.manipulator.Manipulator
import net.exkazuu.gameaiarena.player.ExternalComputerPlayer

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

class AIPlayerStartManipulator(player: Player, com: ExternalComputerPlayer) extends StartManipulator {
  override def runProcessing() {
    com.writeLine(_game.stringify(player))
    val line = com.readLine
    if (line != null) {
      val name = line.trim
      if (!name.isEmpty) {
        _name = name
      }
    }
  }
}

class InternalAIPlayerStartManipulator(player: Player, manipulator: InternalManipulator) extends StartManipulator {
  override def runProcessing() {
    _name = manipulator.getClass().getName()
  }
}
