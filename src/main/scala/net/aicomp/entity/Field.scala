package net.aicomp.entity

import scala.collection.mutable
import scala.util.Random

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

  def moveSquad(p: Point, d: Direction, amount: Int) = {
    val srcTile = this(p)
    val dstTile = this(p + d.p)
    if (srcTile.availableRobots < amount) {
      throw new CommandException("The number of moving robots should be less than or equal to the number of existing movable robots.")
    }
    srcTile.robots -= amount
    dstTile.robots += amount
    dstTile.movedRobots += amount
  }

  def build(player: Player, p: Point, ins: Installation) = {
    val tile = this(p)
    if (!tile.ownedBy(player)) {
      throw new CommandException("You should own a tile where an installation is built.")
    }
    if (tile.installation.isDefined) {
      throw new CommandException("A tile where an installation is built should have no installation.")
    }
    tile.installation = Some(ins)
  }

  def clearMovedRobots() {
    tiles.values.foreach(t => t.movedRobots = 0)
  }

  def calculateMaterialAmount(p: Point, player: Player) = {
    val baseMaterial = if (this(p) ownedBy player) 1 else 0
    val around = Direction.all.map(_.p + p).filter(_.within(radius))
    val aroundMaterial = around.map(apply).count(tile =>
      tile.ownedBy(player) &&
        tile.installation.exists(_ == Installation.mt))
    baseMaterial + aroundMaterial
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
  def apply(radius: Int, players: List[Player]): Field = {
    require(players.length == 3)

    // first, generate 1/3 pattern
    // region: (x, y) such that y >= 0 && x + y >= 1
    val random = new Random(0)
    val field = mutable.Map(Field(radius).tiles.toSeq: _*)
    for (y <- 0 to radius; x <- -y + 1 to -y + radius) {
      // TODO: hole frequency
      if (random.nextInt(5) == 0) {
        field(Point(x, y)).isHole = true
      }
    }
    // second, decide initial position
    val initialY = random.nextInt(radius + 1)
    val initialX = random.nextInt(radius) + 1 - initialY
    field(Point(initialX, initialY)).owner = Some(players(0))
    field(Point(initialX, initialY)).installation = Some(Installation.initial)
    // third, expand the pattern
    def copyTile(tile: Tile, player: Player) = {
      val copiedTile = tile.clone
      if (copiedTile.owner.isDefined) {
        copiedTile.owner = Some(player)
      }
      copiedTile
    }
    for (y <- 0 to radius; x <- -y to -y + radius) {
      val p = Point(x, y)
      field(p.rotate120) = copyTile(field(p), players(1))
      field(p.rotate240) = copyTile(field(p), players(2))
    }
    new Field(radius, field.toMap)
  }
}
