package net.aicomp.terraforming.scene

import net.exkazuu.gameaiarena.gui.Scene
import net.aicomp.terraforming.entity.GameSetting
import net.aicomp.terraforming.entity.GameEnvironment

abstract class ResultScene(nextScene: Scene[GameEnvironment]) extends AbstractScene {
  final override def run() = {
    displayLine("Player1: " + game.field.calculateScore(game.players(0)))
    displayLine("Player2: " + game.field.calculateScore(game.players(1)))
    displayLine("Player3: " + game.field.calculateScore(game.players(2)))
    nextScene
  }
}
