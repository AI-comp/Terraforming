package net.aicomp.scene

import jp.ac.waseda.cs.washi.gameaiarena.gui.Scene
import net.aicomp.entity.Game
import net.aicomp.entity.GameEnvironment
import net.aicomp.util.misc.DateUtils
import net.aicomp.util.settings.Defaults
import net.aicomp.entity.GameSetting

abstract class PlayerScene(nextScene: Scene[GameEnvironment], setting: GameSetting = GameSetting())
  extends CommandBaseScene {
  override def initialize() {
    describe("Enter player names")
    displayLine("Please enter player names with space delimiters.")
  }

  override def execute(names: List[String]) = {
    if (names.size <= 1) {
      displayLine("Please enter two or more names.")
      (game, this)
    } else {
      displayLine(names.size + " players have joined the game. (" + names.mkString(", ") + ")")
      (new Game(), nextScene)
    }
  }
}