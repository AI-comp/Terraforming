package net.aicomp.entity

import net.exkazuu.gameaiarena.gui.Environment
import net.exkazuu.gameaiarena.gui.GamePanel

class GameEnvironment(panel: GamePanel) extends Environment(panel) {
  var game: Game = null
}

object GameEnvironment {
  def apply(panel: GamePanel = null) = {
    new GameEnvironment(panel)
  }
}
