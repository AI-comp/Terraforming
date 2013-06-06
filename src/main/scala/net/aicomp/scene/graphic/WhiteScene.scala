package net.aicomp.scene.graphic

import jp.ac.waseda.cs.washi.gameaiarena.gui.Renderer
import net.aicomp.scene.AbstractScene
import net.aicomp.util.misc.ImageLoader
import net.aicomp.entity.Squad

trait WhiteScene extends AbstractScene {
  // should be defined in a property
  val xDefault = 275
  val yDefault = 170
  // should be defined in a property
  val tileSize = 32
  val xSquadSize = 10
  val ySquadSize = 9
  val xNumSize = 6
  val yNumSize = 9

  override def draw() = {
    val renderer = getRenderer()

    drawInitialMap(renderer)
  }

  def drawInitialMap(renderer: Renderer) = {
    val backgrounds = ImageLoader.loadBackgrounds(renderer)
    val squad = new Squad
    val tiles = Array.tabulate(10, 10) {
      (y: Int, x: Int) => (x, y)
    }

    renderer.drawImage(backgrounds.get(32).get, 0, 0)
    drawTiles(renderer, tiles)
    drawSquadAndNumOnTile(renderer, squad)
  }

  // following methods must be another module.

  // draw tiles in a map
  def drawTiles(renderer: Renderer, tiles: Array[Array[(Int, Int)]]) = {
    val tileImages = ImageLoader.loadTiles(renderer)

    for (tile <- tiles) {
      for ((x, y) <- tile) {
        val ordinates = transToOrthogonal(x, y)
        renderer.drawImage(tileImages.get(32).get, ordinates._1, ordinates._2)
      }
    }
  }

  // transformation tile coordinate to orthogonal coordinate
  def transToOrthogonal(xTile: Int, yTile: Int) = {
    // should be defined in a property
    val tileSize = 32
    // should be final
    val xCenter = xDefault - tileSize / 2
    val yCenter = yDefault - tileSize / 2

    val xOrth = xCenter + tileSize * xTile + (tileSize / 2) * yTile
    val yOrth = yCenter + (3 * tileSize / 4) * yTile

    (xOrth, yOrth)
  }

  // draw squad and num of them on a tile
  def drawSquadAndNumOnTile(renderer: Renderer, squad: Squad) = {
    drawSquadOnTile(renderer, squad)
    drawNumOnTile(renderer, squad)
  }

  // draw squad on a tile
  def drawSquadOnTile(renderer: Renderer, squad: Squad) = {
    val squadImages = ImageLoader.loadRobots(renderer)
    // dummy data
    val tile = (3, 2)
    val ordinates = transToOrthogonal(tile._1, tile._2)

    val xSquad = ordinates._1 + (tileSize / 2) - (xSquadSize / 2)
    val ySquad = ordinates._2 + (tileSize / 2) - (ySquadSize * 0)
    
    // pattern matching by team squad belongs to
    // not yet implemented
    renderer.drawImage(squadImages.get(1).get, xSquad, ySquad)
  }

  // draw num of squad on a tile
  def drawNumOnTile(renderer: Renderer, squad: Squad) = {
	val numImages = ImageLoader.loadNumbers(renderer)
	// dummy data
    val tile = (3, 2)
    val ordinates = transToOrthogonal(tile._1, tile._2)
	
    val xNum1 = ordinates._1 + (tileSize / 2) - (3 * xNumSize / 2)
    val yNum1 = ordinates._2 + (tileSize / 2) - yNumSize
    val xNum2 = ordinates._1 + (tileSize / 2) - (xNumSize / 2)
    val yNum2 = ordinates._2 + (tileSize / 2) - yNumSize
    val xNum3 = ordinates._1 + (tileSize / 2) + (xNumSize / 2)
    val yNum3 = ordinates._2 + (tileSize / 2) - yNumSize
    
    // pattern matching by num of squad
	// not yet implemented
    renderer.drawImage(numImages.get(1,0).get, xNum1, yNum1)
    renderer.drawImage(numImages.get(1,6).get, xNum2, yNum2)
    renderer.drawImage(numImages.get(1,3).get, xNum3, yNum3)
  }
}