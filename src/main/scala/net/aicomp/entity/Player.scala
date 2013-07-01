package net.aicomp.entity

import net.exkazuu.gameaiarena.runner.AbstractRunner

class Player(val id: Int,
  val startManipulator: AbstractRunner[Game, String, String] = null,
  val gameManipulator: AbstractRunner[Game, Array[String], String] = null) {

  require(id >= 0, "id should be non-negative integer")

  var name: String = id.toString()
}
