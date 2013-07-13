package net.aicomp.terraforming.scene

import net.exkazuu.gameaiarena.gui.Scene
import net.aicomp.terraforming.entity.GameSetting
import net.aicomp.terraforming.entity.GameEnvironment

class ResultScene(nextScene: Scene[GameEnvironment]) extends AbstractScene {
  final override def run() = {
    displayLine("Player 1 Score: " + game.field.calculateScore(game.players(0)))
    displayLine("Player 2 Score: " + game.field.calculateScore(game.players(1)))
    displayLine("Player 3 Score: " + game.field.calculateScore(game.players(2)))
    nextScene
  }
}
