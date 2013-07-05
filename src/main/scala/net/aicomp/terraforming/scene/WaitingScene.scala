package net.aicomp.terraforming.scene

import net.aicomp.terraforming.entity.GameEnvironment
import net.exkazuu.gameaiarena.gui.DefaultScene
import net.exkazuu.gameaiarena.gui.Scene

abstract class WaitingScene(nextScene: Scene[GameEnvironment]) extends AbstractScene {
  final override def run() = {
    if (getInputer().isPush(0))
      nextScene
    else
      this
  }
}