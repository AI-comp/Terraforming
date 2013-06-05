package net.aicomp.entity

sealed trait Tile {
  def hasObstacle = false
  var building: String = null
}

case class Land(squad: Squad) extends Tile
