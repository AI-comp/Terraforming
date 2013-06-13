package net.aicomp.scene.graphic

import jp.ac.waseda.cs.washi.gameaiarena.gui.Renderer
import net.aicomp.entity.Field
import net.aicomp.entity.Point
import net.aicomp.scene.AbstractScene
import net.aicomp.util.misc.ImageLoader
import net.aicomp.entity.OrthogonalPoint
import net.aicomp.entity.OrthogonalPoint._
import net.aicomp.entity.Tile
import net.aicomp.entity.Player

trait WhiteScene extends AbstractScene {
  // TODO should be defined in a property
  val defaultX = 500
  val defaultY = 250
  // TODO should be defined in a property
  val pointSize = 32
  val robotSizeX = 10
  val robotSizeY = 9
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
    val tiles = field.tiles

    renderer.drawImage(backgrounds.get(32).get, 0, 0)
    drawPoints(renderer, points)
    drawRobotAndNumOnPoint(renderer, tiles)
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

  // draw robot and num of them on a point
  def drawRobotAndNumOnPoint(renderer: Renderer, tiles: Map[Point, Tile]) = {

    tiles.foreach {
      tile =>
        val num = tile._2.robots
        // val num = tile._2.availableRobots
        if (num > 0) {
          val op = OrthogonalPoint.pointToOrthogonalPoint(tile._1)
          val owner = tile._2.owner
          owner match {
            case Some(o) => {
              drawRobotOnPoint(renderer, op, o.id)
              drawNumOnPoint(renderer, op, o.id, num)
            }
            case _ =>
          }
        }
    }
  }

  // draw robot on a point
  def drawRobotOnPoint(renderer: Renderer, op: OrthogonalPoint, oId: Int = -1) = {
    val robotImages = ImageLoader.loadRobots(renderer)

    val robotX = op.x + (pointSize / 2) - (robotSizeX / 2)
    val robotY = op.y + (pointSize / 2) - (robotSizeY * 0)

    renderer.drawImage(robotImages.get(oId).get, robotX, robotY)
  }

  // draw num of robot on a tile
  def drawNumOnPoint(renderer: Renderer, op: OrthogonalPoint, oId: Int = -1, num: Int) = {
    val numImages = ImageLoader.loadNumbers(renderer)

    // TODO now, num of robots is assumed to be from 0 to 999
    val num1 = (num / 100) % 10
    val num1X = op.x + (pointSize / 2) - (3 * numSizeX / 2)
    val num1Y = op.y + (pointSize / 2) - numSizeY
    
    val num2 = (num / 10) % 10
    val num2X = op.x + (pointSize / 2) - (numSizeX / 2)
    val num2Y = op.y + (pointSize / 2) - numSizeY
    
    val num3 = num % 10
    val num3X = op.x + (pointSize / 2) + (numSizeX / 2)
    val num3Y = op.y + (pointSize / 2) - numSizeY

    renderer.drawImage(numImages.get(oId, num1).get, num1X, num1Y)
    renderer.drawImage(numImages.get(oId, num2).get, num2X, num2Y)
    renderer.drawImage(numImages.get(oId, num3).get, num3X, num3Y)
  }
}