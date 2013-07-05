package net.aicomp.terraforming.scene

import net.aicomp.terraforming.entity.GameEnvironment
import net.exkazuu.gameaiarena.gui.Scene

abstract class EmptyScene(nextScene: Scene[GameEnvironment]) extends AbstractScene {
  final override def run() = {
    nextScene
  }

  final override def runManipulator = throw new Exception()

  final override def runWithCommandString(commandString: String) = throw new Exception()
}