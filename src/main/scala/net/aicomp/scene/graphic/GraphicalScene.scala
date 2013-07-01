package net.aicomp.scene.graphic

import net.aicomp.entity.OrthogonalPoint
import net.aicomp.entity.OrthogonalPoint.numSize
import net.aicomp.entity.OrthogonalPoint.pointSize
import net.aicomp.entity.OrthogonalPoint.pointToOrthogonalPoint
import net.aicomp.entity.OrthogonalPoint.robotSize
import net.aicomp.entity.OrthogonalPoint.roundNumberSize
import net.aicomp.entity.OrthogonalPoint.roundSlashSize
import net.aicomp.entity.Point
import net.aicomp.entity.Tile
import net.aicomp.scene.AbstractScene
import net.aicomp.util.misc.ImageLoader

trait GraphicalScene extends AbstractScene {

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
    drawRound()
    drawRobotAndNumAndInstOnPoint(tiles)
  }

  // following methods must be another module.

  // draw points in a map
  def drawPoints(points: Set[Point]) = {
    val pointImages = ImageLoader.loadTiles(renderer)

    for (point <- points) {
      drawPoint(point, game.field.tiles(point))
    }
  }

  def drawPoint(op: OrthogonalPoint, tile: Tile) = {
    val pointImages = ImageLoader.loadTiles(renderer)
    val imgKey = tile.owner.map { p => "32_" + p.id }.getOrElse("32")

    renderer.drawImage(pointImages.get(imgKey).get, op.x, op.y)
  }
  
  // draw round(current turn / max turn)
  def drawRound() = {
    val roundSlash = ImageLoader.loadRoundSlash(renderer)
    val roundPosition = new OrthogonalPoint(84,10)
    
    renderer.drawImage(roundSlash, roundPosition.x, roundPosition.y)
    drawRoundNumber(game.currentTurn, roundPosition.x, roundPosition.y)
    drawRoundNumber(game.maxTurn, roundPosition.x + roundSlashSize.x + 3 * roundNumberSize.x, roundPosition.y)
  }
  
  def drawRoundNumber(value: Int, numX: Int, numY: Int) = {
    val roundNumbers = ImageLoader.loadRoundNumber(renderer)
    var x = numX
    
    value.toString.reverse.foreach { d =>
      x -= roundNumberSize.x
      renderer.drawImage(roundNumbers(d.toInt - '0'.toInt), x, numY)
    }   
  }

  // draw robot and num of them on a point
  def drawRobotAndNumAndInstOnPoint(tiles: Map[Point, Tile]) = {
    tiles.foreach {
      case (point, tile) =>
        val num = tile.robots
        val op = OrthogonalPoint.pointToOrthogonalPoint(point)

        drawInstallationOnPoint(op, tile)

        if (num > 0) {
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

  def drawInstallationOnPoint(op: OrthogonalPoint, tile: Tile) = {
    val x = op.x + (pointSize.x / 2) + robotSize.x - 2
    val y = op.y + (pointSize.y / 2) + robotSize.y / 2

    tile.installation.map { inst =>
      renderer.drawString(inst.head.toString(), x, y)
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
