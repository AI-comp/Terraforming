package net.aicomp.scene.graphic

import jp.ac.waseda.cs.washi.gameaiarena.gui.Renderer
import net.aicomp.entity.Field
import net.aicomp.entity.Point
import net.aicomp.scene.AbstractScene
import net.aicomp.util.misc.ImageLoader
import net.aicomp.entity.OrthogonalPoint
import net.aicomp.entity.OrthogonalPoint._

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
    val field = game.field
    val points = field.points

    renderer.drawImage(backgrounds.get(32).get, 0, 0)
    drawPoints(renderer, points)
    drawSquadAndNumOnPoint(renderer)
  }

  // following methods must be another module.

  // draw points in a map
  def drawPoints(renderer: Renderer, points: Set[Point]) = {
    val pointImages = ImageLoader.loadTiles(renderer)

    for (point <- points) {
      drawPoint(renderer, point)
    }
  }

  def drawPoint(renderer: Renderer, op: OrthogonalPoint) = {
    val pointImages = ImageLoader.loadTiles(renderer)

    renderer.drawImage(pointImages.get(32).get, op.x, op.y)
  }

  // draw squad and num of them on a point
  def drawSquadAndNumOnPoint(renderer: Renderer) = {
    // TODO this dummy data should be replaced with squad's position
    val point = Point(0, 0)

    drawSquadOnPoint(renderer, point)
    drawNumOnPoint(renderer, point)
  }

  // draw squad on a point
  def drawSquadOnPoint(renderer: Renderer, op: OrthogonalPoint) = {
    val squadImages = ImageLoader.loadRobots(renderer)

    val squadX = op.x + (pointSize / 2) - (squadSizeX / 2)
    val squadY = op.y + (pointSize / 2) - (squadSizeY * 0)

    // TODO pattern matching by team squad belong to
    renderer.drawImage(squadImages.get(1).get, squadX, squadY)
  }

  // draw num of squad on a tile
  def drawNumOnPoint(renderer: Renderer, op: OrthogonalPoint) = {
    val numImages = ImageLoader.loadNumbers(renderer)

    val num1X = op.x + (pointSize / 2) - (3 * numSizeX / 2)
    val num1Y = op.y + (pointSize / 2) - numSizeY
    val num2X = op.x + (pointSize / 2) - (numSizeX / 2)
    val num2Y = op.y + (pointSize / 2) - numSizeY
    val num3X = op.x + (pointSize / 2) + (numSizeX / 2)
    val num3Y = op.y + (pointSize / 2) - numSizeY

    // TODO pattern matching by num of squad
    renderer.drawImage(numImages.get(1, 0).get, num1X, num1Y)
    renderer.drawImage(numImages.get(1, 6).get, num2X, num2Y)
    renderer.drawImage(numImages.get(1, 3).get, num3X, num3Y)
  }
}