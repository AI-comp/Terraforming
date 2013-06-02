package net.aicomp.entity

case class Tile(val kind: Tile.Kind, squad: Squad = null) {

  val squadOpt: Option[Squad] = Option[Squad](squad)

  def isOccupied = kind == Tile.Kind.Occupied
  def isVacant = kind == Tile.Kind.Vacant
  def isHole = kind == Tile.Kind.Hole
}

object Tile {
  sealed class Kind private(val value: Int)
  object Kind {
    val Occupied = new Kind(1)
    val Vacant = new Kind(2)
    val Hole = new Kind(3)
  }
}
