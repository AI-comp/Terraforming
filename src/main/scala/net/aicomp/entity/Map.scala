package net.aicomp.entity

import math.{max, min}

/***********************************
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
 ***********************************/

class Map(radius: Int) {
  require(radius > 0, "radius should be positive integer")

  val tiles: Array[Array[Tile]] = Array.tabulate(radius*2+1, radius*2+1) {
    (y: Int, x: Int) => new Tile(Tile.Kind.Vacant)
  }

  def apply(x: Int, y: Int): Tile = tiles(y+radius)(x+radius)
  def apply(p: Point): Tile = this(p.x, p.y)

  def update(x: Int, y: Int, t: Tile) { tiles(y+radius)(x+radius) = t }
  def update(p: Point, t: Tile) { this(p.x, p.y) = t }

  def foreach[T](f: Tile=>T): Unit = for (p <- indices) f(this(p))

  def indices: List[Point] = for (
    y <- List.range(-radius, radius+1);
    x <- List.range(max(-radius, -y-radius), min(radius, radius-y)+1)
  ) yield Point(x, y)

  def moveSquad(p: Point, d: Direction) = {
    val t = this(p)
    this(p) = new Tile(Tile.Kind.Vacant)
    this(p+d.p) = new Tile(Tile.Kind.Occupied, t.squadOpt.get)
  }

  override def toString: String = {
    val height = radius*6+5
    val width  = radius*12+7
    val ss = Array.tabulate(height) { _ => new StringBuilder(width, " "*width) }
    def printTile(p: Point, t: Tile) {
      val x = p.x
      val y = p.y
      ss(y-2)(x)   = '*'
      ss(y-1)(x-3) = '*'
      ss(y-1)(x+3) = '*'
      ss(y+1)(x-3) = '*'
      ss(y+1)(x+3) = '*'
      ss(y+2)(x)   = '*'

      ss(y-2)(x-1) = ','
      ss(y-2)(x+1) = ','
      ss(y+2)(x-1) = '\''
      ss(y+2)(x+1) = '\''

      ss(y-1)(x-2) = '\''
      ss(y-1)(x+2) = '\''
      ss(y+1)(x-2) = ','
      ss(y+1)(x+2) = ','

      ss(y)(x-3) = '|'
      ss(y)(x+3) = '|'

      if (t.squadOpt.nonEmpty) ss(y)(x) = 'R'
    }
    val c = Point((width-1)/2, (height-1)/2)
    val dx = Point(6, 0)
    val dy = Point(3, 3)
    for (p <- indices) {
      printTile(c + dx*p.x + dy*p.y, this(p))
    }
    ss.mkString("\n")
  }

}
