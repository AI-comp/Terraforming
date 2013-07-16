package net.aicomp.terraforming.scene

import net.aicomp.terraforming.entity.Game
import net.aicomp.terraforming.entity.GameEnvironment
import net.aicomp.terraforming.entity.GameSetting
import net.exkazuu.gameaiarena.gui.Scene
import net.exkazuu.gameaiarena.manipulator.ThreadManipulator

class PlayerScene(nextScene: Scene[GameEnvironment],
  manipulators: Vector[ThreadManipulator[Game, Array[String], String]] = Vector())
  extends ManipultorScene(manipulators) {

  override def initialize() {
    describe("Enter player names")
    displayPromptForPlayer()
  }

  override def runWithCommandString(name: String) = {
    require(name != null)

    if (name.trim.length <= 0) {
      displayLine("Please enter a name.")
      this
    } else {
      game.currentPlayer.name = if (name.length() < 20) name else name.substring(0, 20)
      if (game.changePlayerIndex() == 0) {
        displayLine(game.players.size + " players have joined the game. ("
          + game.players.map(_.name).mkString(", ") + ")")
        nextScene
      } else {
        displayPromptForPlayer()
        this
      }
    }
  }

  private def displayPromptForPlayer() {
    displayLine("Please enter a name of Player " + (game.currentPlayerIndex + 1) + ".")
  }
}
