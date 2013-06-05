package net.aicomp.scene.graphic

import jp.ac.waseda.cs.washi.gameaiarena.gui.Renderer
import net.aicomp.scene.AbstractScene
import net.aicomp.util.misc.ImageLoader

trait WhiteScene extends AbstractScene {
  val xDefault = 250
  val yDefault = 100

  override def draw() = {
    val renderer = getRenderer()

    val tiles = Array.tabulate(10, 10) {
      (y: Int, x: Int) => (x, y)
    }

    drawInitialMap(renderer, tiles)
  }

  def drawInitialMap(renderer: Renderer, tiles: Array[Array[(Int, Int)]]) = {
    val backgrounds = ImageLoader.loadBackgrounds(renderer)
    val tileImages = ImageLoader.loadTiles(renderer)

    // Background
    renderer.drawImage(backgrounds.get(32).get, 0, 0)

    for (tile <- tiles) {
      for ((x, y) <- tile) {
        val ordinates = transToOrthogonal(x, y)
        renderer.drawImage(tileImages.get(32).get, ordinates._1, ordinates._2)
      }
    }
  }

  // following methods must be another module.

  // transformation tile coordinate to orthogonal coordinate
  def transToOrthogonal(xTile: Int, yTile: Int) = {
    // final
    val tileSize = 32
    val xCenter = xDefault - tileSize / 2
    val yCenter = yDefault - tileSize / 2

    val xOrth = xCenter + tileSize * xTile + (tileSize / 2) * yTile
    val yOrth = yCenter + (3 * tileSize / 4) * yTile

    (xOrth, yOrth)
  }

}