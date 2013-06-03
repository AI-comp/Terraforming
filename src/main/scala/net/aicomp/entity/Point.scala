package net.aicomp.entity
import math.{abs, max}

case class Direction private(val p: Point)

object Direction {
  val ur = new Direction(new Point( 1,-1))
  val ul = new Direction(new Point( 0,-1))
  val r  = new Direction(new Point( 1, 0))
  val dr = new Direction(new Point( 0, 1))
  val dl = new Direction(new Point(-1, 1))
  val l  = new Direction(new Point(-1, 0))

  private val strMap: scala.collection.Map[String, Direction] = scala.collection.Map(
    "ur" -> ur,
    "ul" -> ul,
    "r"  ->  r,
    "dr" -> dr,
    "dl" -> dl,
    "l"  ->  l
  )

  def fromString(s: String): Direction = strMap(s)
}

case class Point(val x: Int, val y: Int) {
  def +(r: Point) = Point(x+r.x, y+r.y)
  def -(r: Point) = Point(x-r.x, y-r.y)
  def *(r: Int) = Point(x*r, y*r)
  def distance(p: Point) = {
    val dx = x - p.x
    val dy = y - p.y
    max(max(abs(dx), abs(dy)), abs(dx+dy))
  }
  def within(width: Int) =
    abs(x) <= width && abs(y) <= width && abs(x+y) <= width
  override def toString() = "(" + x + "," + y + ")"
}

object Point {
  def pointsWithin(width: Int) : Set[Point] =
    (for (x <- -width to width;
         y <- -width to width;
         val p = new Point(x, y);
         if p.within(width)) yield p).toSet
}
