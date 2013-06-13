package net.aicomp.entity

class Player(val name: String, val id: Int) {
  require(id >= 0, "id should be non-negative integer")

  def stringify: String = id + " " + name
}
