package net.aicomp.entity

sealed trait Tile {
  def hasObstacle = false
}

case class Land(squad: Squad) extends Tile
