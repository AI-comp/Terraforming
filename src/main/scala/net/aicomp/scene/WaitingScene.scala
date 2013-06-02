package net.aicomp.scene

import jp.ac.waseda.cs.washi.gameaiarena.gui.Scene
import net.aicomp.entity.Game
import net.aicomp.entity.GameEnvironment

abstract class WaitingScene(val nextScene: Scene[GameEnvironment]) extends CommandBaseScene {
  override def execute() = {
    if (getInputer().isPush(0))
      (game, nextScene)
    else
      (game, this)
  }

  override def nextCommand: Option[List[String]] = throw new Exception()

  override def execute(words: List[String]): (Game, Scene[GameEnvironment]) = throw new Exception()
}