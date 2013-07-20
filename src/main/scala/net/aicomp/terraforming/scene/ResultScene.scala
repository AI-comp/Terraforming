package net.aicomp.terraforming.scene

import net.exkazuu.gameaiarena.gui.Scene
import net.aicomp.terraforming.entity.GameSetting
import net.aicomp.terraforming.entity.GameEnvironment

class ResultScene(nextScene: Scene[GameEnvironment]) extends AbstractScene {
  final override def run() = {
    val s1 = "Player 1 (" + game.players(0).name + ") Score: " + game.field.calculateScore(game.players(0))
    val s2 = "Player 2 (" + game.players(1).name + ") Score: " + game.field.calculateScore(game.players(1))
    val s3 = "Player 3 (" + game.players(2).name + ") Score: " + game.field.calculateScore(game.players(2))
    displayLine(s1)
    displayLine(s2)
    displayLine(s3)
    println("------------------ Result Suammary (it is showed somtimes redundantly) ------------------")
    println(s1)
    println(s2)
    println(s3)
    nextScene
  }
}
