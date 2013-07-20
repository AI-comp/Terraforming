package net.aicomp.terraforming.entity

import net.exkazuu.gameaiarena.manipulator.ThreadManipulator

case class Player(val id: Int, var name: String) {
  require(id >= 0, "id should be non-negative integer")
}

object Player {
  def apply(id: Int) = {
    new Player(id, id.toString())
  }
}