package net.aicomp.terraforming.scene

import net.aicomp.terraforming.entity.Game
import net.aicomp.terraforming.entity.GameEnvironment
import net.aicomp.terraforming.entity.GameSetting
import net.exkazuu.gameaiarena.gui.Scene
import net.exkazuu.gameaiarena.manipulator.ThreadManipulator

abstract class PlayerScene(nextScene: Scene[GameEnvironment],
  manipulators: Vector[ThreadManipulator[Game, Array[String], String]] = Vector())
  extends ManipultorScene(manipulators) {

  override def initialize() {
    describe("Enter player names")
    displayLine("Please enter three player names without spaces.")
  }

  override def runWithCommandString(name: String) = {
    require(name != null)

    if (name.trim.length <= 0) {
      displayLine("Please enter a name.")
      this
    } else {
      game.currentPlayer.name = name
      if (game.changePlayerIndex() == 0) {
        displayLine(game.players.size + " players have joined the game. ("
          + game.players.map(_.name).mkString(", ") + ")")
        nextScene
      } else this
    }
  }
}