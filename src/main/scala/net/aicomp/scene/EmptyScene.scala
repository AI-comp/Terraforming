package net.aicomp.scene

import net.aicomp.entity.GameEnvironment
import net.exkazuu.gameaiarena.gui.Scene

abstract class EmptyScene(nextScene: Scene[GameEnvironment]) extends AbstractScene {
  final override def run() = {
    nextScene
  }

  final override def runManipulator = throw new Exception()

  final override def runWithCommandString(commandString: String) = throw new Exception()
}