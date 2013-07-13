package net.aicomp.terraforming.entity

import java.util.Random
import scala.util.parsing.json.JSONArray

/*
 ***********************************
 *                                 *
 *           radius = 2            *
 *                                 *
 *         ,*,   ,*,   ,*,         *
 *       *'   '*'   '*'   '*       *
 *       | 0 -2|     | 2 -2|       *
 *      ,*,   .*,   .*,   .*,      *
 *    *'   '*'   '*'   '*'   '*    *
 *    |     | 0 -1| 1 -1|     |    *
 *   ,*,   .*,   .*,   .*,   .*,   *
 * *'   '*'   '*'   '*'   '*'   '* *
 * |-2 0 |-1 0 | 0 0 | 1 0 | 2 0 | *
 * *,   ,*,   .*,   .*,   .*,   .* *
 *   '*'   '*'   '*'   '*'   '*'   *
 *    |     |-1 1 | 0 1 |     |    *
 *    *,   ,*,   .*,   .*,   ,*    *
 *      '*'   '*'   '*'   '*'      *
 *       |-2 2 |     | 0 2 |       *
 *       *,   .*,   .*,   .*       *
 *         '*'   '*'   '*'         *
 *                                 *
 ***********************************
 */
class Field(val radius: Int, val tiles: Map[Point, Tile]) {
  require(radius > 0, "radius should be positive integer")

  def points: Set[Point] = tiles.keySet
  def apply(x: Int, y: Int): Tile = tiles(new Point(x, y))
  def apply(p: Point): Tile = tiles(p)

  def clearMovedRobots() {
    tiles.values.foreach(t => t.movedRobots = 0)
  }

  //Commands
  def moveSquad(player: Player, p: Point, d: Direction, amount: Int) = {
    if (!points.contains(p)) {
      throw new CommandException("You cannot choose an outer tile of the field")
    }
    if (!points.contains(p + d)) {
      throw new CommandException("Robots cannot go out from the field")
    }

    val srcTile = this(p)
    val dstTile = this(p + d)
    // check ALL constraints before any change written
    srcTile.checkLeave(player, amount)
    dstTile.checkEnter(player, amount)
    srcTile.leave(player, amount)
    dstTile.enter(player, amount)
  }

  def build(player: Player, p: Point, ins: Installation) = {
    if (!points.contains(p)) {
      throw new CommandException("You cannot choose an outer tile of the field")
    }

    if (!Installation.buildables.contains(ins)) {
      throw new CommandException("You cannot choose this installation")
    }

    val tile = this(p)

    //tile check
    if (!tile.ownedBy(player)) {
      throw new CommandException("You should own a tile where an installation is built.")
    }
    if (tile.installation.isDefined) {
      throw new CommandException("A tile where an installation is built should have no installation.")
    }
    if (tile.isHole) {
      if (ins != Installation.bridge) {
        throw new CommandException("Installations other than bridge are not allowed to be built on a hole")
      }
    } else {
      if (ins == Installation.bridge) {
        throw new CommandException("Bridge is not allowed to be built except for a hole.")
      }
    }

    //cost check
    if (tile.robots < ins.robotCost) {
      throw new CommandException("The number of robots is not enough.")
    }
    val aroundMaterial = aroundMaterialAmount(p, player)
    if (aroundMaterial < ins.materialCost) {
      throw new CommandException("Although it is " + ins.materialCost + " material cost necessity for building " + ins.name + ", only " + aroundMaterial + " costs are obtained from this tile.")
    }

    tile.isHole = false
    tile.robots -= ins.robotCost
    tile.installation = Some(ins)
    ins match {
      case Installation.town =>
        availableAroundTiles(p).filter(_.ownedBy(player)).filter(_.installation.isEmpty)
          .foreach { _.installation = Some(Installation.house) }
        tile.additionalScore = aroundMaterial - 9
      case _ =>
    }
  }

  //Effects of Installation
  def produceRobot(player: Player) = {
    for ((p, tile) <- tiles) {
      if (tile.ownedBy(player)) {
        tile.installation match {
          case Some(Installation.initial) =>
            tile.enter(player, 5)
          case Some(Installation.robotmaker) =>
            tile.enter(player, 2)
          case _ =>
        }
      }
    }
  }

  //AttackTower
  def attack(player: Player) = {
    for ((p, tile) <- tiles) {
      if (tile.ownedBy(player)) {
        tile.installation match {
          case Some(Installation.tower) =>
            availableLineTiles(p, 2).filter(!_.ownedBy(player)).foreach { t =>
              t.robots = math.max(t.robots - 2, 0)
            }
          case _ =>
        }
      }
    }
  }

  def ownedTiles(player: Player) = {
    tiles.values.filter(_.ownedBy(player))
  }

  def ownedTileAmount(player: Player) = {
    ownedTiles(player).size
  }

  def additionalScoreAmount(player: Player) = {
    ownedTiles(player).map(_.additionalScore).sum
  }

  def robotAmount(player: Player) = {
    ownedTiles(player).map(_.robots).sum
  }

  def eachInstallationAmount(player: Player, installation: Installation) = {
    ownedTiles(player).count(_.installation.exists(_ == installation))
  }

  def installationAmount(player: Player) = {
    ownedTiles(player).count(_.installation.isDefined)
  }

  def availableAroundPoints(p: Point, length: Int = 1) = p.aroundPoints(length).filter(_.within(radius))

  def availableLinePoints(p: Point, length: Int = 1) = p.linePoints(length).filter(_.within(radius))

  def availableAroundTiles(p: Point, length: Int = 1) = availableAroundPoints(p, length).map(apply)

  def availableLineTiles(p: Point, length: Int = 1) = availableLinePoints(p, length).map(apply)

  def materialAmount(p: Point, player: Player) = {
    if (this(p) existBaseMaterialOf player) {
      1 + availableAroundTiles(p).count(tile => tile.ownedBy(player) && tile.installation.exists(_ == Installation.excavator))
    } else {
      0
    }
  }

  def aroundMaterialAmount(p: Point, player: Player) = {
    val baseAmount = materialAmount(p, player)
    val aroundMount = availableAroundPoints(p).map(materialAmount(_, player)).sum
    baseAmount + aroundMount
  }

  def calculateScore(player: Player) = tiles.values.foldLeft(0)(_ + _.score(player))

  def stringify: String =
    (radius + 1) + " " + tiles.size + "\n" + points.toList.sorted.map { p =>
      val material = tiles(p).owner match { case Some(owner) => materialAmount(p, owner) case _ => 0 }
      p.stringify + " " + tiles(p).stringify(material) + "\n"
    }.mkString

  def toPartialJson = {
    points.toList.sorted.map { p =>
      val material = tiles(p).owner match { case Some(owner) => materialAmount(p, owner) case _ => 0 }
      Map(
        "x" -> p.x,
        "y" -> p.y) ++ tiles(p).toJsonMap(material)
    }.toList
  }

  override def toString: String = {
    val height = radius * 6 + 5
    val width = radius * 12 + 7
    val ss = Array.tabulate(height) { _ => new StringBuilder(width, " " * width) }
    def printTile(p: Point, t: Tile) {
      val x = p.x
      val y = p.y
      ss(y - 2)(x) = '*'
      ss(y - 1)(x - 3) = '*'
      ss(y - 1)(x + 3) = '*'
      ss(y + 1)(x - 3) = '*'
      ss(y + 1)(x + 3) = '*'
      ss(y + 2)(x) = '*'

      ss(y - 2)(x - 1) = ','
      ss(y - 2)(x + 1) = ','
      ss(y + 2)(x - 1) = '\''
      ss(y + 2)(x + 1) = '\''

      ss(y - 1)(x - 2) = '\''
      ss(y - 1)(x + 2) = '\''
      ss(y + 1)(x - 2) = ','
      ss(y + 1)(x + 2) = ','

      ss(y)(x - 3) = '|'
      ss(y)(x + 3) = '|'

      ss(y)(x) = if (t.isHole) {
        'H'
      } else if (t.robots > 0) {
        'R'
      } else {
        t.installation match {
          case Some(x) => x.head
          case None => ' '
        }
      }
    }
    val c = Point((width - 1) / 2, (height - 1) / 2)
    val dx = Point(6, 0)
    val dy = Point(3, 3)
    for (p <- points) {
      printTile(c + dx * p.x + dy * p.y, this(p))
    }
    ss.mkString("\n")
  }
}

object Field {
  /** Generates empty field */
  def apply(radius: Int) = {
    new Field(radius, Point.pointsWithin(radius).map(p => (p -> new Tile())).toMap)
  }

  /** Generates field at random */
  def apply(radius: Int, players: IndexedSeq[Player])(implicit random: Random): Field = {
    require(players.length == 3)

    def generateOneAThirdField() = {
      // first, generate 1/3 pattern
      var field = Map(Field(radius).tiles.toSeq: _*)
      var holeCount = 0

      // region: (x, y) such that y >= 0 && x + y >= 1
      for (y <- 0 to radius; x <- -y + 1 to -y + radius) {
        field(Point(x, y)).owner = None
        field(Point(x, y)).installation = None

        if (random.nextInt(5) == 0) {
          field(Point(x, y)).isHole = true
          holeCount += 1
        } else {
          field(Point(x, y)).isHole = false
        }
      }

      // second, decide initial position
      val initialY = random.nextInt(radius + 1)
      val initialX = random.nextInt(radius) + 1 - initialY
      val initial = Point(initialX, initialY)
      field(initial).owner = Some(players(0))
      field(initial).installation = Some(Installation.initial)
      if (field(initial).isHole) {
        holeCount -= 1
        field(initial).isHole = false;
      }

      (field, holeCount, initial)
    }

    def copyTile(tile: Tile, player: Player) = {
      val copiedTile = tile.clone
      if (copiedTile.owner.isDefined) {
        copiedTile.owner = Some(player)
      }
      copiedTile
    }

    @scala.annotation.tailrec
    def generate(field: Map[Point, Tile], holeCount: Int, initialPoint: Point): Field = {
      val buffer = field.toBuffer

      // third expanded pattern
      for (y <- 0 to radius; x <- -y + 1 to -y + radius) {
        val p = Point(x, y)
        buffer += (p.rotate120) -> copyTile(field(p), players(1))
        buffer += (p.rotate240) -> copyTile(field(p), players(2))
      }
      val generatedField = new Field(radius, buffer.toMap)

      // fourth validate field
      val shortestPaths = initialPoint.shortestPathToEachPoint(generatedField, (p => !field(p).isHole))
      if (holeCount < 15
        && shortestPaths.size * 2 >= (generatedField.tiles.size - holeCount * players.length)
        && shortestPaths.contains(initialPoint.rotate120)) {
        return generatedField
      } else {
        val (field, holeCount, initialPoint) = generateOneAThirdField()
        generate(field, holeCount, initialPoint)
      }
    }

    generate _ tupled (generateOneAThirdField)
  }
}
