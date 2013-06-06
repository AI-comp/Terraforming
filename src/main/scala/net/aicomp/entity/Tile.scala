package net.aicomp.entity

sealed trait Tile {
  def hasObstacle = building != null
  var building: String = null
}

case class Land(squad: Squad) extends Tile
