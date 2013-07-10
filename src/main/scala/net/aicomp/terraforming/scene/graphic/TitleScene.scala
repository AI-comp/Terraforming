package net.aicomp.terraforming.scene.graphic

import net.aicomp.terraforming.scene.AbstractScene
import net.aicomp.terraforming.util.misc.ImageLoader
import java.awt.Color

trait TitleScene extends AbstractScene {
  override def draw() = {
    val renderer = getRenderer()
    val title = ImageLoader.loadTitle(renderer)
    renderer.drawImage(title, 0, 0)
  }
}