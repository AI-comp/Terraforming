package net.aicomp.scene.graphic

import jp.ac.waseda.cs.washi.gameaiarena.gui.Renderer
import net.aicomp.scene.AbstractScene
import net.aicomp.util.misc.ImageLoader
import net.aicomp.entity.Squad
import net.aicomp.entity.Field
import net.aicomp.entity.Point

trait WhiteScene extends AbstractScene {
  // TODO should be defined in a property
  val defaultX = 500
  val defaultY = 250
  // TODO should be defined in a property
  val pointSize = 32
  val squadSizeX = 10
  val squadSizeY = 9
  val numSizeX = 6
  val numSizeY = 9

  override def draw() = {
    val renderer = getRenderer()

    drawInitialMap(renderer)
  }

  def drawInitialMap(renderer: Renderer) = {
    val backgrounds = ImageLoader.loadBackgrounds(renderer)
    val squad = new Squad
    val field = new Field(7)
    val points = field.indices

    renderer.drawImage(backgrounds.get(32).get, 0, 0)
    drawPoints(renderer, points)
    drawSquadAndNumOnPoint(renderer, squad)
  }

  // following methods must be another module.

  // draw points in a map
  def drawPoints(renderer: Renderer, points: List[Point]) = {
    val pointImages = ImageLoader.loadTiles(renderer)

    for (point <- points) {
      val (orthX, orthY) = transToOrthogonal(point.x, point.y)
      renderer.drawImage(pointImages.get(32).get, orthX, orthY)
    }
  }

  // transformation point coordinate to orthogonal coordinate
  def transToOrthogonal(pointX: Int, pointY: Int) = {
    // TODO should be defined in a property
    // TODO should be final
    val centerX = defaultX - pointSize / 2
    val centerY = defaultY - pointSize / 2

    val orthX = centerX + pointSize * pointX + (pointSize / 2) * pointY
    val orthY = centerY + (3 * pointSize / 4) * pointY

    (orthX, orthY)
  }

  // draw squad and num of them on a point
  def drawSquadAndNumOnPoint(renderer: Renderer, squad: Squad) = {
    drawSquadOnPoint(renderer, squad)
    drawNumOnPoint(renderer, squad)
  }

  // draw squad on a point
  def drawSquadOnPoint(renderer: Renderer, squad: Squad) = {
    val squadImages = ImageLoader.loadRobots(renderer)
    // dummy data
    // TODO replace squad's position to a dummy data
    val point = (0, 0)
    val (orthX, orthY) = transToOrthogonal(point._1, point._2)

    val squadX = orthX + (pointSize / 2) - (squadSizeX / 2)
    val squadY = orthY + (pointSize / 2) - (squadSizeY * 0)

    // TODO pattern matching by team squad belong to
    renderer.drawImage(squadImages.get(1).get, squadX, squadY)
  }

  // draw num of squad on a tile
  def drawNumOnPoint(renderer: Renderer, squad: Squad) = {
    val numImages = ImageLoader.loadNumbers(renderer)
    // TODO following dummy data should be replaced with squad's position
    val point = (0, 0)
    val (orthX, orthY) = transToOrthogonal(point._1, point._2)

    val num1X = orthX + (pointSize / 2) - (3 * numSizeX / 2)
    val num1Y = orthY + (pointSize / 2) - numSizeY
    val num2X = orthX + (pointSize / 2) - (numSizeX / 2)
    val num2Y = orthY + (pointSize / 2) - numSizeY
    val num3X = orthX + (pointSize / 2) + (numSizeX / 2)
    val num3Y = orthY + (pointSize / 2) - numSizeY

    // TODO pattern matching by num of squad
    renderer.drawImage(numImages.get(1, 0).get, num1X, num1Y)
    renderer.drawImage(numImages.get(1, 6).get, num2X, num2Y)
    renderer.drawImage(numImages.get(1, 3).get, num3X, num3Y)
  }
}