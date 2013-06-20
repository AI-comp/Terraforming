package net.aicomp.entity

// new Point class for rendering
case class OrthogonalPoint(x: Int, y: Int) {
}

object OrthogonalPoint {
  // default center of Map is (500,250)
  val defaultX = 500
  val defaultY = 250
  // image size
  val pointSize = Size(32, 32)
  val robotSize = Size(10, 9)
  val numSize = Size(6, 9)

  case class Size(val x: Int, val y: Int)

  // transformation point coordinate to orthogonal coordinate
  implicit def pointToOrthogonalPoint(p: Point): OrthogonalPoint = {
    // TODO should be defined in a property
    // TODO should be final
    val centerX = defaultX - pointSize.x / 2
    val centerY = defaultY - pointSize.y / 2

    val orthX = centerX + pointSize.x * p.x + (pointSize.x / 2) * p.y
    val orthY = centerY + (3 * pointSize.y / 4) * p.y

    OrthogonalPoint(orthX, orthY)
  }

  def orthogonalPointToPoints(orthP: OrthogonalPoint, field: Field): Set[Point] = {
    field.points.filter(p => {
      val op = pointToOrthogonalPoint(p)
      (orthP.x >= op.x) && (orthP.x < op.x + pointSize.x) && (orthP.y >= -(orthP.x / 2) + (op.x / 2) + op.y + (pointSize.x / 4)) && (orthP.y > (orthP.x / 2) - (op.x / 2) + op.y - (pointSize.x / 4)) && (orthP.y < -(orthP.x / 2) + (op.x / 2) + op.y + (5 * pointSize.x / 4)) && (orthP.y <= (orthP.x / 2) - (op.x / 2) + op.y + (3 * pointSize.x / 4))
    })
  }
}
