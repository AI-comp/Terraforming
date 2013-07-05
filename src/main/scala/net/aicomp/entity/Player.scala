package net.aicomp.entity

import net.exkazuu.gameaiarena.manipulator.ThreadManipulator

class Player(val id: Int,
  val startManipulator: ThreadManipulator[Game, Array[String], String] = null,
  val gameManipulator: ThreadManipulator[Game, Array[String], String] = null) {

  require(id >= 0, "id should be non-negative integer")

  var name: String = id.toString()
}
