package net.aicomp.entity

import net.exkazuu.gameaiarena.manipulator.Manipulator

class Player(val id: Int,
  val startManipulator: Manipulator[Game, String, String] = null,
  val gameManipulator: Manipulator[Game, Array[String], String] = null) {

  require(id >= 0, "id should be non-negative integer")

  var name: String = id.toString()
}
