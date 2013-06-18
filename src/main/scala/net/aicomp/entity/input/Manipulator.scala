package net.aicomp.entity.input

import net.aicomp.entity.Game
import net.exkazuu.gameaiarena.runner.AbstractRunner

/**
 * A class to manipulate players.
 *
 * This class is designed by the Decorator pattern.
 * GameAIArena can add features such as recording or time limitation to objects of this class.
 */
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
