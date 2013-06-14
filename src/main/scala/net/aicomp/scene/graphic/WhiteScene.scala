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

  override def draw() = {

    drawMap()
  }

  def drawMap() = {
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
      case (point, tile) =>
        val num = tile.robots
        if (num > 0) {
          val op = OrthogonalPoint.pointToOrthogonalPoint(point)
          
          tile.owner match {
            case Some(owner) => {
              val numString = "%03d".format(num)             
              drawRobotOnPoint(op, owner.id)
              drawNumOnPoint(numString, op, owner.id)
            }
            case _ =>
          }
        }
    }
  }

  // draw robot on a point
  def drawRobotOnPoint(op: OrthogonalPoint, ownerId: Int = -1) = {
    val robotImages = ImageLoader.loadRobots(renderer)

    val robotX = op.x + (pointSize.x / 2) - (robotSize.x / 2)
    val robotY = op.y + (pointSize.y / 2) - (robotSize.y * 0)

    renderer.drawImage(robotImages.get(ownerId).get, robotX, robotY)
  }

  // draw num of robot on a tile
  // note align: left
  def drawNumOnPoint(numString: String, op: OrthogonalPoint, ownerId: Int = -1) = {
    val numImages = ImageLoader.loadNumbers(renderer)

    val drawY = op.y + (2 * numSize.y / 3)
    var drawX = op.x

    numString.foreach(d => {
      drawX += numSize.x
      renderer.drawImage(numImages.get(ownerId, d.toString().toInt).get, drawX, drawY)
    })
  }
}