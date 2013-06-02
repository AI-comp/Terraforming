package net.aicomp.entity

import jp.ac.waseda.cs.washi.gameaiarena.gui.Environment
import jp.ac.waseda.cs.washi.gameaiarena.gui.GamePanel

/**
 * ゲーム全体の情報を統括するクラスです。
 */
class GameEnvironment(panel: GamePanel) extends Environment(panel) {
  var game = new Game()
}

object GameEnvironment {
  def apply(panel: GamePanel = null) = {
    new GameEnvironment(panel)
  }
}
