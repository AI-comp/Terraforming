package net.aicomp.terraforming.entity

// new Point class for rendering
case class OrthogonalPoint(x: Int, y: Int) {
}

object OrthogonalPoint {
  // default center of Map is (500,250)
  val defaultX = 320
  val defaultY = 250
  val statusX = 700
  val statusY = 50
  // image size
  val pointSize = Size(48, 48)
  val robotSize = Size(10, 9)
  val numSize = Size(6, 9)
  val roundSlashSize = Size(32, 32)
  val roundNumberSize = Size(24, 32)
  val statusSize = Size(250, 120)

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
      isInthePoint(orthP, op)
    })
  }

  // judge whether target point is in a specific Tile 
  def isInthePoint(target: OrthogonalPoint, op: OrthogonalPoint): Boolean = {
    // six linear equations which compose each regular hexagon(let the upper left of its image be (op.x, op.y)) are as follows:
    // x                      = op.x
    // x                      = op.x + pointSize.x
    // y - op.y               = -(1/2){x - (op.x + (1/2)pointSize)}
    // y - op.y               =  (1/2){x - (op.x + (1/2)pointSize)}
    // y - (op.y - pointSize) = -(1/2){x - (op.x + (1/2)pointSize)}
    // y - (op.y - pointSize) =  (1/2){x - (op.x + (1/2)pointSize)}
    (target.x >= op.x) && (target.x < op.x + pointSize.x) && (target.y >= -(target.x / 2) + (op.x / 2) + op.y + (pointSize.x / 4)) && (target.y > (target.x / 2) - (op.x / 2) + op.y - (pointSize.x / 4)) && (target.y < -(target.x / 2) + (op.x / 2) + op.y + (5 * pointSize.x / 4)) && (target.y <= (target.x / 2) - (op.x / 2) + op.y + (3 * pointSize.x / 4))
  }
}
