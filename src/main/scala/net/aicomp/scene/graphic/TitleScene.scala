package net.aicomp.scene.graphic

import net.aicomp.scene.AbstractScene
import net.aicomp.util.misc.ImageLoader

trait TitleScene extends AbstractScene {
  override def draw() = {
    val renderer = getRenderer()
    ImageLoader.prefetch(renderer)
    val title = ImageLoader.loadTitle(renderer)
    renderer.drawImage(title, 0, 0)
  }
}