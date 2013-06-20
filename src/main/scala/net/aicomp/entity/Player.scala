package net.aicomp.entity

import net.aicomp.input.Manipulator

class Player(val id: Int, val manipulator: Manipulator = null) {
  require(id >= 0, "id should be non-negative integer")

  var name: String = id.toString()
}
