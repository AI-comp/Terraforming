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
    drawPlayers()
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
    val roundPosition = new OrthogonalPoint(84, 10)

    renderer.drawImage(roundSlash, roundPosition.x, roundPosition.y)
    drawRoundNumber(game.currentTurn, roundPosition.x, roundPosition.y)
    drawRoundNumber(game.maxTurn, roundPosition.x + OrthogonalPoint.roundSlashSize.x + 3 * OrthogonalPoint.roundNumberSize.x, roundPosition.y)
  }

  def drawRoundNumber(value: Int, numX: Int, numY: Int) = {
    val roundNumbers = ImageLoader.loadRoundNumber(renderer)
    var x = numX

    value.toString.reverse.foreach { d =>
      x -= OrthogonalPoint.roundNumberSize.x
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
    val x = op.x + (OrthogonalPoint.pointSize.x / 2) + OrthogonalPoint.robotSize.x - 2
    val y = op.y + (OrthogonalPoint.pointSize.y / 2) + OrthogonalPoint.robotSize.y / 2

    tile.installation.map { inst =>
      renderer.drawString(inst.head.toString(), x, y)
    }
  }

  // draw robot on a point
  def drawRobotOnPoint(op: OrthogonalPoint, ownerId: Int = -1) = {
    val robotImages = ImageLoader.loadRobots(renderer)

    val robotX = op.x + (OrthogonalPoint.pointSize.x / 2) - (OrthogonalPoint.robotSize.x / 2)
    val robotY = op.y + (OrthogonalPoint.pointSize.y / 2) - (OrthogonalPoint.robotSize.y * 0)

    renderer.drawImage(robotImages.get(ownerId).get, robotX, robotY)
  }

  // draw num of robot on a tile
  // note align: left
  def drawNumOnPoint(numString: String, op: OrthogonalPoint, ownerId: Int = -1) = {
    val numImages = ImageLoader.loadNumbers(renderer)

    val drawY = op.y + (2 * OrthogonalPoint.numSize.y / 3)
    var drawX = op.x

    numString.foreach(d => {
      drawX += OrthogonalPoint.numSize.x
      renderer.drawImage(numImages.get(ownerId, d.toString().toInt).get, drawX, drawY)
    })
  }

  def drawPlayers() {
    // font
    val font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    val scoreX = OrthogonalPoint.statusX + OrthogonalPoint.statusSize.x
    val robotAmountX = OrthogonalPoint.statusX + OrthogonalPoint.statusSize.x / 5
    val installationsAmountX = OrthogonalPoint.statusX + OrthogonalPoint.statusSize.x / 5
    val eachInstallationAmountX = OrthogonalPoint.statusX + 3 * OrthogonalPoint.statusSize.x / 5

    game.players.foreach { player =>

      // player name
      val nameY = OrthogonalPoint.statusY + OrthogonalPoint.statusSize.y / 6 + OrthogonalPoint.statusSize.y * player.id
      renderer.drawString(player.name, OrthogonalPoint.statusX, nameY, Color.WHITE, font)

      // score
      val scoreY = OrthogonalPoint.statusY + OrthogonalPoint.statusSize.y / 6 + OrthogonalPoint.statusSize.y * player.id
      drawScore(player, scoreX, scoreY)

      // robots
      val robotAmountY = (OrthogonalPoint.statusY + 3 * OrthogonalPoint.statusSize.y / 6 - OrthogonalPoint.numSize.y) + OrthogonalPoint.statusSize.y * player.id
      drawTotalRobotAmount(player, robotAmountX, robotAmountY)

      // installations
      val installationsAmountY = (OrthogonalPoint.statusY + 5 * OrthogonalPoint.statusSize.y / 6 - OrthogonalPoint.numSize.y) + OrthogonalPoint.statusSize.y * player.id
      drawTotalBuildableInstallationAmount(player, installationsAmountX, installationsAmountY)

      // each installation
      val eachInstallationAmountY = (OrthogonalPoint.statusY + 2 * OrthogonalPoint.statusSize.y / 6) + OrthogonalPoint.statusSize.y * player.id
      drawEachInstallationAmount(player, eachInstallationAmountX, eachInstallationAmountY)
    }
  }

  def drawScore(player: Player, x: Int, y: Int) {
    val numImages = ImageLoader.loadNumbers(renderer)
    var drawX = x

    game.field.calculateScore(player).toString().reverse.foreach { n =>
      renderer.drawImage(numImages.get(-1, n.toString.toInt).get, drawX, y)
      drawX -= OrthogonalPoint.numSize.x
    }
  }

  def drawTotalRobotAmount(player: Player, x: Int, y: Int) {
    val robotImages = ImageLoader.loadRobots(renderer)
    val numImages = ImageLoader.loadNumbers(renderer)
    var drawX = x

    // TODO (x of robotImage) is assumed to be (x of numImage - 38)
    renderer.drawImage(robotImages.get(player.id).get, x - 38, y)
    game.field.robotAmount(player).toString().reverse.foreach { n =>
      renderer.drawImage(numImages.get(-1, n.toString.toInt).get, drawX, y)
      drawX -= OrthogonalPoint.numSize.x
    }
  }

  def drawTotalBuildableInstallationAmount(player: Player, x: Int, y: Int) {
    val numImages = ImageLoader.loadNumbers(renderer)
    var drawX = x

    game.field.installationAmount(player).toString.reverse.foreach { n =>
      renderer.drawImage(numImages.get(-1, n.toString.toInt).get, drawX, y)
      drawX -= OrthogonalPoint.numSize.x
    }
  }

  def drawEachInstallationAmount(player: Player, x: Int, y: Int) {
    val numImages = ImageLoader.loadNumbers(renderer)
    // TODO load installations image.

    var drawX = x
    var drawY = y

    // factory
    // TODO render factory image.
    game.field.eachInstallationAmount(player, Installation.buildables(0)).toString.reverse.foreach { n =>
      renderer.drawImage(numImages.get(-1, n.toString.toInt).get, drawX, drawY)
      drawX -= OrthogonalPoint.numSize.x
    }

    drawX = x
    drawY += OrthogonalPoint.statusSize.y / 6
    // bridge
    // TODO render bridge image.
    game.field.eachInstallationAmount(player, Installation.buildables(1)).toString.reverse.foreach { n =>
      renderer.drawImage(numImages.get(-1, n.toString.toInt).get, drawX, drawY)
      drawX -= OrthogonalPoint.numSize.x
    }

    drawX = x
    drawY += OrthogonalPoint.statusSize.y / 6
    // shield
    // TODO render shield image.
    game.field.eachInstallationAmount(player, Installation.buildables(2)).toString.reverse.foreach { n =>
      renderer.drawImage(numImages.get(-1, n.toString.toInt).get, drawX, drawY)
      drawX -= OrthogonalPoint.numSize.x
    }

    drawX = x
    drawY += OrthogonalPoint.statusSize.y / 6
    // attack
    // TODO render attack image.
    game.field.eachInstallationAmount(player, Installation.buildables(3)).toString.reverse.foreach { n =>
      renderer.drawImage(numImages.get(-1, n.toString.toInt).get, drawX, drawY)
      drawX -= OrthogonalPoint.numSize.x
    }

    drawX = x + 4 * OrthogonalPoint.statusSize.x / 10
    drawY = y
    // pit
    // TODO render pit image.
    game.field.eachInstallationAmount(player, Installation.buildables(4)).toString.reverse.foreach { n =>
      renderer.drawImage(numImages.get(-1, n.toString.toInt).get, drawX, drawY)
      drawX -= OrthogonalPoint.numSize.x
    }

    drawX = x + 4 * OrthogonalPoint.statusSize.x / 10
    drawY += OrthogonalPoint.statusSize.y / 6
    // house
    // TODO render house image.
    game.field.eachInstallationAmount(player, Installation.buildables(5)).toString.reverse.foreach { n =>
      renderer.drawImage(numImages.get(-1, n.toString.toInt).get, drawX, drawY)
      drawX -= OrthogonalPoint.numSize.x
    }

    drawX = x + 4 * OrthogonalPoint.statusSize.x / 10
    drawY += OrthogonalPoint.statusSize.y / 6
    // town
    // TODO render town image.
    game.field.eachInstallationAmount(player, Installation.buildables(6)).toString.reverse.foreach { n =>
      renderer.drawImage(numImages.get(-1, n.toString.toInt).get, drawX, drawY)
      drawX -= OrthogonalPoint.numSize.x
    }

    drawX = x + 4 * OrthogonalPoint.statusSize.x / 10
    drawY += OrthogonalPoint.statusSize.y / 6
    // city
    // TODO render city image.
    game.field.eachInstallationAmount(player, Installation.buildables(7)).toString.reverse.foreach { n =>
      renderer.drawImage(numImages.get(-1, n.toString.toInt).get, drawX, drawY)
      drawX -= OrthogonalPoint.numSize.x
    }
  }
}
