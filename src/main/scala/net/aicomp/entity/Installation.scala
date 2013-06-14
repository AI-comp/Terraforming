package net.aicomp.entity

object Installation {

  val initial = new Installation("initial", 0)
  val factory = new Installation("factory", 50)
  val bridge = new Installation("bridge", 10)
  val shield = new Installation("shield", 25)
  val attack = new Installation("attack", 25)
  val pit = new Installation("pit", 20)
  val park = new Installation("park", 1)
  val square = new Installation("square", 1)
  val public = new Installation("public", 1, 10)


  val buildables = List(factory, bridge, shield, attack, pit, park, square, public)
}

class Installation(val name: String, val cost: Int, val score: Int = 0) {
  def head = name.head

  override def toString = name
}
