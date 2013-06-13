package net.aicomp.entity

import jp.ac.waseda.cs.washi.gameaiarena.gui.Renderer

// new Point class for rendering
case class OrthogonalPoint(x: Int, y: Int) {
}

object OrthogonalPoint {
  // TODO should be defined in a property
  val defaultX = 500
  val defaultY = 250
  // TODO should be defined in a property
  val pointSize = 32
  val numSizeX = 6
  val numSizeY = 9

  // transformation point coordinate to orthogonal coordinate
  implicit def pointToOrthogonalPoint(p: Point): OrthogonalPoint = {
    // TODO should be defined in a property
    // TODO should be final
    val centerX = defaultX - pointSize / 2
    val centerY = defaultY - pointSize / 2

    val orthX = centerX + pointSize * p.x + (pointSize / 2) * p.y
    val orthY = centerY + (3 * pointSize / 4) * p.y

    OrthogonalPoint(orthX, orthY)
  }

  def orthogonalPointToPoints(orthP: OrthogonalPoint, field: Field): Set[Point] = {
    field.points.filter(p => {
      val op = pointToOrthogonalPoint(p)
      op.x <= orthP.x && orthP.x < op.x + pointSize && op.y <= orthP.y && orthP.y < op.y + pointSize
    })
  }
}
