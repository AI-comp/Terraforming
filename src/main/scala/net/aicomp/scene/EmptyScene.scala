package net.aicomp.scene

import jp.ac.waseda.cs.washi.gameaiarena.gui.Scene
import net.aicomp.entity.Game
import net.aicomp.entity.GameEnvironment

/**
 * 何もしないシーンクラスです．
 * タイトル画面を表示する際などに利用してください．
 */
abstract class EmptyScene(val nextScene: Scene[GameEnvironment]) extends AbstractScene {
  final override def run() = {
    nextScene
  }

  final override def nextCommand: Option[List[String]] = throw new Exception()

  final override def runWithArgs(words: List[String]): Scene[GameEnvironment] = throw new Exception()
}