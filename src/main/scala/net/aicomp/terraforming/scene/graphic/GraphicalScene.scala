package net.aicomp.terraforming.scene.graphic

import net.aicomp.terraforming.entity.OrthogonalPoint
import net.aicomp.terraforming.entity.OrthogonalPoint.pointToOrthogonalPoint
import net.aicomp.terraforming.entity.Point
import net.aicomp.terraforming.entity.Tile
import net.aicomp.terraforming.scene.AbstractScene
import net.aicomp.terraforming.util.misc.ImageLoader
import net.aicomp.terraforming.entity.Player
import net.aicomp.terraforming.entity.Installation
import java.awt.Font
import java.awt.Color

trait GraphicalScene extends AbstractScene {

  val statusPosition = new OrthogonalPoint(720, 20)
  val roundPosition = new OrthogonalPoint(910, 445)

  // image size
  val robotSize = Size(10, 9)
  val numberSize = Size(6, 9)
  val statusNumberSize = Size(12, 18)
  val roundSlashSize = Size(32, 32)
  val roundNumberSize = Size(24, 32)
  val statusSize = Size(280, 120)

  case class Size(val x: Int, val y: Int)

  // font
  val font = new Font(Font.MONOSPACED, Font.PLAIN, 26);

  override def draw() = {
    drawMap()
  }

  def drawMap() = {
    val background = ImageLoader.loadBackground(renderer)
    val field = game.field
    val points = field.points
    val tiles = field.tiles

    renderer.drawImage(background, 0, 0)

    for (point <- points) {
      drawPoint(point, game.field.tiles(point))
    }

    drawDataOnPoint(tiles)
    drawPlayers()
    drawRound()
  }

  // following methods must be another module.

  def drawPoint(op: OrthogonalPoint, tile: Tile) = {
    val pointImages = ImageLoader.loadTiles(renderer)
    val imgKey = tile.owner.map { p => "48_" + p.id }.getOrElse("48")
    renderer.drawImage(pointImages.get(imgKey).get, op.x, op.y)
  }

  // draw round(current turn / max turn)
  def drawRound() = {
    val round = ImageLoader.loadRoundParts(renderer)

    renderer.drawImage(round("Title"), roundPosition.x - 210, roundPosition.y)
    renderer.drawImage(round("Slash"), roundPosition.x, roundPosition.y)
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

  // draw robot and number of them on a point
  def drawDataOnPoint(tiles: Map[Point, Tile]) = {
    tiles.foreach {
      case (point, tile) =>
        val number = tile.robots
        val op = pointToOrthogonalPoint(point)

        drawInstallationOnPoint(op, tile)
        tile.owner match {
          case Some(owner) => {
            val numberString = "%03d".format(number)
            drawRobotOnPoint(op, owner.id)
            drawNumberOnPoint(numberString, op, owner.id)
          }
          case _ =>
        }
    }
  }

  def drawInstallationOnPoint(op: OrthogonalPoint, tile: Tile) = {
    val pointSize = OrthogonalPoint.pointSize
    val x = op.x + 15
    val y = op.y + 6

    val instImages = ImageLoader.loadInstallations(renderer)
    if (tile.isHole) {
      renderer.drawImage(instImages("hole"), x - 7, y)
    }

    tile.owner match {
      case Some(owner) => {
        tile.installation.map { inst =>
          renderer.drawImage(instImages(inst.name.toString() + "_" + owner.id), x, y)
          //renderer.drawString(inst.head.toString(), x, y)
        }
      }
      case _ =>
    }
  }

  // draw robot on a point
  def drawRobotOnPoint(op: OrthogonalPoint, ownerId: Int = -1) = {
    val pointSize = OrthogonalPoint.pointSize
    val robotImages = ImageLoader.loadRobots(renderer)

    val robotX = op.x + 8
    val robotY = op.y + 30

    renderer.drawImage(robotImages.get(ownerId).get, robotX, robotY)
  }

  // draw number of robot on a tile
  // note align: left
  def drawNumberOnPoint(numString: String, op: OrthogonalPoint, ownerId: Int = -1) = {
    val numberImages = ImageLoader.loadNumbers(renderer)

    var drawX = op.x + 14
    val drawY = op.y + 30

    numString.foreach(d => {
      drawX += numberSize.x
      renderer.drawImage(numberImages.get(ownerId, d.toString().toInt).get, drawX, drawY)
    })
  }

  def drawPlayers() {
    val robotAmountX = statusPosition.x + 61
    val eachInstallationAmountX = statusPosition.x + 61
    val settlementAmountX = statusPosition.x + 127
    val holeAmountX = statusPosition.x + 193
    val scoreX = statusPosition.x + 259
    val installationsAmountX = statusPosition.x + 259
    val marginY = 20

    val playerInfoBg = ImageLoader.loadPlayerInformationBackgrounds(renderer)
    val robotImages = ImageLoader.loadLargeRobots(renderer)
    val instImages = ImageLoader.loadStatusInstallations(renderer)

    game.players.foreach { player =>

      val infoBgY = statusPosition.y + (statusSize.y + marginY) * player.id
      renderer.drawImage(playerInfoBg(player.id), statusPosition.x, infoBgY)

      // player name
      val nameY = statusPosition.y + 28 + (statusSize.y + marginY) * player.id
      renderer.drawString(player.name , statusPosition.x + 10, nameY, Color.WHITE, font)

      // score
      val scoreY = statusPosition.y + 15 + (statusSize.y + marginY) * player.id
      drawStatusNumber(game.field.calculateScore(player), scoreX, scoreY)

      val amountY = 43 + statusPosition.y + (statusSize.y + marginY) * player.id
      val installationStatusY = 71

      // robots
      renderer.drawImage(robotImages.get(player.id).get, robotAmountX - 52, amountY)
      drawStatusNumber(game.field.robotAmount(player), robotAmountX, amountY)

      // settlements
      drawStatusNumber(game.field.installationAmount(player), settlementAmountX, amountY)

      // holes
      drawStatusNumber(game.field.installationAmount(player), holeAmountX, amountY)

      // installations
      drawStatusNumber(game.field.installationAmount(player), installationsAmountX, amountY)

      // each installation
      val eachInstallationAmountY = statusPosition.y + 71 + (statusSize.y + marginY) * player.id
      renderer.drawImage(instImages("town_"+player.id),eachInstallationAmountX - 49 + 2 * 66, eachInstallationAmountY + 1 * 24)

      Installation.buildables.iterator.zipWithIndex.foreach {
        case (installation, index) => {
          drawStatusNumber(game.field.eachInstallationAmount(player, installation),
            eachInstallationAmountX + (index % 4) * 66, eachInstallationAmountY + (index / 4) * 24)
        }
      }
    }
  }

  def drawStatusNumber(number: Int, x: Int, y: Int) {
    val numberImages = ImageLoader.loadStatusNumbers(renderer)
    var drawX = x
    number.toString().reverse.foreach { n =>
      renderer.drawImage(numberImages.get(-1, n.toString.toInt).get, drawX, y)
      drawX -= statusNumberSize.x
    }
  }

}
