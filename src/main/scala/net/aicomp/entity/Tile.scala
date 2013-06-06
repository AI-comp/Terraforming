package net.aicomp.entity

sealed trait Tile {
  def hasObstacle = false
}

case class Hole() extends Tile {
  override def hasObstacle = true
}

case class Land(val squad: Squad) extends Tile

case class DevelopedLand(val building: String) extends Tile {
  override def hasObstacle = true
}

case class InitialPosition(val player: Player) extends Tile
