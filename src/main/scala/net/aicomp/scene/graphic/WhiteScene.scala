package net.aicomp.scene.graphic

import jp.ac.waseda.cs.washi.gameaiarena.gui.Renderer
import net.aicomp.entity.Field
import net.aicomp.entity.Point
import net.aicomp.scene.AbstractScene
import net.aicomp.util.misc.ImageLoader
import net.aicomp.entity.OrthogonalPoint
import net.aicomp.entity.OrthogonalPoint._
import net.aicomp.entity.Tile

trait WhiteScene extends AbstractScene {
  // TODO should be defined in a property
  val defaultX = 500
  val defaultY = 250
  // TODO should be defined in a property
  val pointSize = Size(32, 32)
  val robotSize = Size(10, 9)
  val numSize = Size(6, 9)

  class Size(val x: Int, val y: Int)
  object Size {
    def apply(x: Int, y: Int) = new Size(x, y)
  }

  override def draw() = {

    drawInitialMap()
  }

  def drawInitialMap() = {
    val backgrounds = ImageLoader.loadBackgrounds(renderer)
    val field = game.field
    val points = field.points
    val tiles = field.tiles

    renderer.drawImage(backgrounds.get(32).get, 0, 0)
    drawPoints(points)
    drawRobotAndNumOnPoint(tiles)
  }

  // following methods must be another module.

  // draw points in a map
  def drawPoints(points: Set[Point]) = {
    val pointImages = ImageLoader.loadTiles(renderer)

    for (point <- points) {
      drawPoint(point, game.field.tiles(point))
    }
  }

  def drawPoint(op : OrthogonalPoint, tile : Tile) = {
    val pointImages = ImageLoader.loadTiles(renderer)
    val imgKey = tile.owner.map{p => "32_" + p.id}.getOrElse("32")

    renderer.drawImage(pointImages.get(imgKey).get, op.x, op.y)
  }

  // draw robot and num of them on a point
  def drawRobotAndNumOnPoint(tiles: Map[Point, Tile]) = {

    tiles.foreach {
      tile =>
        val num = tile._2.robots
        // val num = tile._2.availableRobots
        if (num > 0) {
          val op = OrthogonalPoint.pointToOrthogonalPoint(tile._1)
          val owner = tile._2.owner
          owner match {
            case Some(o) => {
              drawRobotOnPoint(op, o.id)
              drawNumOnPoint("%03d", num, op, o.id)
            }
            case _ =>
          }
        }
    }
  }

  // draw robot on a point
  def drawRobotOnPoint(op: OrthogonalPoint, oId: Int = -1) = {
    val robotImages = ImageLoader.loadRobots(renderer)

    val robotX = op.x + (pointSize.x / 2) - (robotSize.x / 2)
    val robotY = op.y + (pointSize.y / 2) - (robotSize.y * 0)

    renderer.drawImage(robotImages.get(oId).get, robotX, robotY)
  }

  // draw num of robot on a tile
  // note align: left
  def drawNumOnPoint(format: String, num: Int, op: OrthogonalPoint, oId: Int = -1) = {
    val numImages = ImageLoader.loadNumbers(renderer)

    val drawY = op.y + (2 * numSize.y / 3)
    var drawX = op.x

    format.format(num).foreach(d => {
      drawX += numSize.x
      renderer.drawImage(numImages.get(oId, d.toString().toInt).get, drawX, drawY)
    })
  }
}