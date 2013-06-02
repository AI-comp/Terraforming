package net.aicomp.scene

import jp.ac.waseda.cs.washi.gameaiarena.gui.Scene
import net.aicomp.entity.GameEnvironment

abstract class MainScene(val nextScene: Scene[GameEnvironment]) extends CommandBaseScene {
  def execute(commandAndArgs: List[String]) = {
    (game, this)
  }
}