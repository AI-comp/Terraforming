package net.aicomp.entity
import math.{abs, max}

case class Point(val x: Int, val y: Int) {
  def +(r: Point) = new Point(x+r.x, y+r.y)
  def -(r: Point) = new Point(x-r.x, y-r.y)
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
