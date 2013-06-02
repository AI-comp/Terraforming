package net.aicomp.scene.graphic

import jp.ac.waseda.cs.washi.gameaiarena.gui.Scene
import net.aicomp.entity.GameEnvironment
import net.aicomp.util.misc.ImageLoader
import net.aicomp.scene.AbstractScene

trait TitleScene extends AbstractScene {
  override def draw() = {
    val renderer = getRenderer()
    ImageLoader.prefetch(renderer)
    val title = ImageLoader.loadTitle(renderer)
    renderer.drawImage(title, 0, 0)
  }
}