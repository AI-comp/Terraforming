package net.aicomp.terraforming.entity

import scala.annotation.migration
import scala.collection.mutable.ListBuffer
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
    val rad = field.radius
    val paths = shortestPathToEachPoint(field, isMovable)
    paths.get(Point(goal.x + rad, goal.y + rad))
  }

  def shortestPathToEachPoint(field: Field, isMovable: Point => Boolean): Map[Point, List[Direction]] = {
    def canEnter(p: Point) = p.within(field.radius) && isMovable(p)
    if (canEnter(this) == false) {
      // invalid starting point
      return Map.empty
    }

    var paths = Map[Point, List[Direction]]()
    val q = new scala.collection.mutable.Queue[Point]
    q += this
    paths += Point(this.x, this.y) -> List.empty
    while (!q.isEmpty) {
      val curr = q.dequeue
      for (dir <- Direction.all) {
        val next = curr + dir
        if (canEnter(next) && !paths.contains(next)) {
          q += next
          paths += next -> (paths(curr).toBuffer :+ dir).toList
        }
      }
    }
    return paths
  }

  def aroundPoints(length: Int = 1) = {
    def aroundPoints(p: Point) = {
      Direction.all.map(_.p + p)
    }
    var set = Set(this)
    for (i <- 1 to length) {
      set = set.map(aroundPoints(_)).flatten
    }
    (set - this).toList
  }
  def linePoints(length: Int = 1) = {
    var buf = ListBuffer[Point]()
    for (i <- 1 to length) {
      buf ++= Direction.all.map { d =>
        (1 to i).foldLeft(this) { (p, _) => p + d }
      }
    }
    buf.toList
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
