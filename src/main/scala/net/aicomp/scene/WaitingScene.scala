package net.aicomp.scene

import net.aicomp.entity.GameEnvironment
import net.exkazuu.gameaiarena.gui.Scene

abstract class WaitingScene(nextScene: Scene[GameEnvironment]) extends AbstractScene {
  final override def run() = {
    if (getInputer().isPush(0))
      nextScene
    else
      this
  }

  final override def runManipulator = throw new Exception()

  final override def runWithCommand(commandString: List[String]) = throw new Exception()
}