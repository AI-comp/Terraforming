package net.aicomp.scene

import jp.ac.waseda.cs.washi.gameaiarena.gui.Scene
import net.aicomp.entity.Game
import net.aicomp.entity.GameEnvironment

abstract class EmptyScene(val nextScene: Scene[GameEnvironment]) extends CommandBaseScene {
  override def execute() = {
    (game, nextScene)
  }

  override def nextCommand: Option[List[String]] = throw new Exception()

  override def execute(words: List[String]): (Game, Scene[GameEnvironment]) = throw new Exception()
}