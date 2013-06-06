package net.aicomp.entity

abstract class Tile {
  def hasObstacle = false
}

case class Hole() extends Tile

case class Land(val squad: Squad) extends Tile

case class DevelopedLand(val building: String) extends Tile

case class InitialPosition(val player: Player) extends Tile
