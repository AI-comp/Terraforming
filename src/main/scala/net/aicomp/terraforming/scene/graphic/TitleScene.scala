package net.aicomp.terraforming.scene.graphic

import net.aicomp.terraforming.scene.AbstractScene
import net.aicomp.terraforming.util.misc.ImageLoader

trait TitleScene extends AbstractScene {
  override def draw() = {
    val renderer = getRenderer()
    ImageLoader.prefetch(renderer)
    val title = ImageLoader.loadTitle(renderer)
    renderer.drawImage(title, 0, 0)
  }
}