package net.aicomp.entity

object Installation {

  val initial = new Installation("initial", 1, 0)
  val factory = new Installation("factory", 50, 4)
  val bridge = new Installation("bridge", 1, 4)
  val shield = new Installation("shield", 25, 6)
  val attack = new Installation("attack", 25, 5)
  val pit = new Installation("pit", 20, 4)
  val park = new Installation("park", 1, 4)
  val square = new Installation("square", 1, 9)
  val public = new Installation("public", 1, 19)

  val buildables = List(factory, bridge, shield, attack, pit, park, square, public)
}

class Installation(val name: String, val robotCost: Int, val materialCost: Int) {
  def head = name.head

  override def toString = name
}
