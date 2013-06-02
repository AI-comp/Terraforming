package net.terraforming.model

case class Point(val x: Int, val y: Int) {
  def +(r: Point) = new Point(x+r.x, y+r.y)
  def -(r: Point) = new Point(x-r.x, y-r.y)
}
