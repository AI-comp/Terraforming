package net.aicomp.entity

object Installation {

  // TODO: rename
  val initial = new Installation("initial", 0)
  val factory = new Installation("factory", 1)
  val bridge = new Installation("bridge", 2)
  val shield = new Installation("shield", 3)
  val attack = new Installation("attack", 4)
  val pit = new Installation("pit", 5)
  val park = new Installation("park", 6)
  val square = new Installation("square", 7)
  val public = new Installation("public", 8)
  

  val buildables = List(factory, bridge, shield, attack, pit, park, square, public)
}

class Installation(val name: String, val cost: Int) {
  def head = name.head

  override def toString = name
}
