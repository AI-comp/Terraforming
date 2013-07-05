package net.aicomp.terraforming.entity

import net.exkazuu.gameaiarena.manipulator.ThreadManipulator

class Player(val id: Int) {
  require(id >= 0, "id should be non-negative integer")

  var name: String = id.toString()
}
