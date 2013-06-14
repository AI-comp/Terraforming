package net.aicomp.entity

import scala.math.abs
import scala.math.max
import scala.math.min

case class Direction private (val p: Point) {
  def +(r: Point): Point = p + r
  def -(r: Point): Point = p - r
}

object Direction {
  val ur = new Direction(new Point(1, -1))
  val ul = new Direction(new Point(0, -1))
  val r = new Direction(new Point(1, 0))
  val dr = new Direction(new Point(0, 1))
  val dl = new Direction(new Point(-1, 1))
  val l = new Direction(new Point(-1, 0))

  val all = List[Direction](ur, ul, r, dr, dl, l)

  private val strMap = Map(
    "ur" -> ur,
    "ul" -> ul,
    "r" -> r,
    "dr" -> dr,
    "dl" -> dl,
    "l" -> l)

  def find = strMap.get _
  def keys = strMap.keys
}

case class Point(val x: Int, val y: Int) extends Ordered[Point] {
  def +(r: Point) = Point(x + r.x, y + r.y)
  def +(r: Direction): Point = this + r.p
  def -(r: Point) = Point(x - r.x, y - r.y)
  def -(r: Direction): Point = this - r.p
  def *(r: Int) = Point(x * r, y * r)
  def compare(p: Point) = if (x != p.x) x - p.x else y - p.y

  def rotate120() = Point(-(x + y), x)
  def rotate240() = Point(y, -(x + y))

  def distance(p: Point): Int = {
    val dx = x - p.x
    val dy = y - p.y
    max(max(abs(dx), abs(dy)), abs(dx + dy))
  }
  def within(radius: Int): Boolean = distance(Point.origin) <= radius
  def shortestPathTo(goal: Point, field: Field, player: Player): Option[List[Direction]] = {
    shortestPathTo(goal, field, p => field(p).isMovable(player))
  }
  def shortestPathTo(goal: Point, field: Field, isMovable: Point => Boolean): Option[List[Direction]] = {
    def canEnter(p: Point) = p.within(field.radius) && isMovable(p)
    if (canEnter(this) == false) {
      // invalid starting point
      return None
    }

    val rad = field.radius
    val paths = Array.ofDim[List[Direction]](rad * 2 + 1, rad * 2 + 1)
    val q = new scala.collection.mutable.Queue[Point]
    q += this
    paths(this.x + rad)(this.y + rad) = List.empty
    while (!q.isEmpty) {
      val curr = q.dequeue
      if (curr == goal) {
        // found
        return Some(paths(curr.x + rad)(curr.y + rad).reverse)
      }
      for (dir <- Direction.all) {
        val next = curr + dir
        if (canEnter(next) && paths(next.x + rad)(next.y + rad) == null) {
          q += next
          paths(next.x + rad)(next.y + rad) = dir :: paths(curr.x + rad)(curr.y + rad)
        }
      }
    }
    // not found
    return None
  }

  def stringify() = x + " " + y
  override def toString() = "(" + x + "," + y + ")"
}

object Point {
  val origin = Point(0, 0)

  /** returns sorted list of points within radius */
  def pointsWithin(radius: Int): List[Point] = for (
    x <- List.range(-radius, radius + 1);
    y <- List.range(max(-radius, -x - radius), min(radius, radius - x) + 1)
  ) yield Point(x, y)
}
