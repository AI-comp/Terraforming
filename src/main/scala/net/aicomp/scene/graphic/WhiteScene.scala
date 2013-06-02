package net.aicomp.scene.graphic

import jp.ac.waseda.cs.washi.gameaiarena.gui.Scene
import net.aicomp.entity.GameEnvironment
import net.aicomp.util.misc.ImageLoader
import java.awt.Color

trait WhiteScene extends Scene[GameEnvironment] {
  override def draw() = {
    val renderer = getRenderer()
    renderer.clear(Color.WHITE)
  }
}