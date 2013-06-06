package net.aicomp.entity

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
class Field(val radius: Int) {
  require(radius > 0, "radius should be positive integer")

  val tiles: Array[Array[Tile]] = Array.tabulate(radius * 2 + 1, radius * 2 + 1) {
    (y, x) => Land(null)
  }
  this(0, 0) = Land(Squad())

  def apply(x: Int, y: Int): Tile = tiles(y + radius)(x + radius)
  def apply(p: Point): Tile = this(p.x, p.y)

  def update(x: Int, y: Int, t: Tile) { tiles(y + radius)(x + radius) = t }
  def update(p: Point, t: Tile) { this(p.x, p.y) = t }

  def foreach[T](f: Tile => T): Unit = for (p <- indices) f(this(p))

  def indices: List[Point] = Point.pointsWithin(radius)

  def moveSquad(p: Point, d: Direction) = {
    val src = this(p)
    src match {
      case Land(squad) => {
        this(p) = Land(null)
        this(p + d.p) = Land(squad)
      }
    }
  }

  def build(p: Point, t: String) = {
    this(p) match {
      case Land(null) => this(p) = DevelopedLand(t)
      case _ => ()
    }
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

      t match {
        case Hole() => ss(y)(x) = 'H'
        case Land(squad) => if (squad != null) ss(y)(x) = 'R'
        case DevelopedLand(building) => if (building != null) ss(y)(x - 2) = 'B'
        case InitialPosition(player) => ss(y)(x - 2) = 'I'
      }
    }
    val c = Point((width - 1) / 2, (height - 1) / 2)
    val dx = Point(6, 0)
    val dy = Point(3, 3)
    for (p <- indices) {
      printTile(c + dx * p.x + dy * p.y, this(p))
    }
    ss.mkString("\n")
  }

}

object Field {
  /** generates field at random */
  def generate(radius: Int, player1: Player, player2: Player, player3: Player)
    : Field = {
    // first, generate 1/3 pattern
    // region: (x, y) such that y >= 0 && x + y >= 1
    val random = new Random(0)
    val field = new Field(radius)
    for (y <- 0 to radius; x <- -y + 1 to -y + radius) {
      // TODO: hole frequency
      field(x, y) = random.nextInt(5) match {
        case 0 => Hole()
        case _ => Land(null)
      }
    }
    // second, decide initial position
    val initialY = random.nextInt(radius + 1)
    val initialX = random.nextInt(radius) + 1 - initialY
    field(initialX, initialY) = InitialPosition(player1)
    // third, expand the pattern
    def copyTile(t: Tile, p: Player) = t match {
      case InitialPosition(_) => InitialPosition(p)
      case t => t
    }
    for (y <- 0 to radius; x <- -y to -y + radius) {
      field(Point(x, y).rotate120) = copyTile(field(x, y), player2)
      field(Point(x, y).rotate240) = copyTile(field(x, y), player3)
    }
    return field
  }
}
