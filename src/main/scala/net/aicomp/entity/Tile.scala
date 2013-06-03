package net.aicomp.entity

sealed trait Tile
case class Land(squad: Squad) extends Tile
