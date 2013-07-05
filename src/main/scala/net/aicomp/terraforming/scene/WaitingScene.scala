package net.aicomp.terraforming.scene

import net.exkazuu.gameaiarena.gui.Scene
import net.aicomp.terraforming.entity.GameEnvironment

abstract class WaitingScene(nextScene: Scene[GameEnvironment]) extends AbstractScene {
  final override def run() = {
    if (getInputer().isPush(0))
      nextScene
    else
      this
  }

  final override def runManipulator = throw new Exception()

  final override def runWithCommandString(commandString: String) = throw new Exception()
}