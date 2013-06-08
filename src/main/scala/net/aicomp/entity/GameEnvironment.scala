package net.aicomp.entity

import jp.ac.waseda.cs.washi.gameaiarena.gui.Environment
import jp.ac.waseda.cs.washi.gameaiarena.gui.GamePanel

class GameEnvironment(panel: GamePanel) extends Environment(panel) {
  var game: Game = null
}

object GameEnvironment {
  def apply(panel: GamePanel = null) = {
    new GameEnvironment(panel)
  }
}
