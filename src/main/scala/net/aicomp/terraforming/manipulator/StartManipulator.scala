package net.aicomp.terraforming.manipulator

import java.util.Scanner

import net.aicomp.terraforming.entity.Game
import net.aicomp.terraforming.entity.Player
import net.aicomp.terraforming.scene.graphic.TextBoxUtils
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

class NopStartManipulator extends StartManipulator {
  override def runProcessing() {}
}

class ConsoleUserStartManipulator(scanner: Scanner) extends StartManipulator {
  override def runProcessing() {
    _name = scanner.nextLine()
  }
}

class GraphicalUserStartManipulator() extends StartManipulator {
  override def runProcessing() {
    TextBoxUtils.inputCommandLists() match {
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
    val className = manipulator.getClass().getName()
    val dotIndex = className.lastIndexOf('.')
    _name = if (dotIndex >= 0) className.substring(dotIndex + 1) else className
  }
}
