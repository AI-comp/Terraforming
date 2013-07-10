package net.aicomp.terraforming.entity

object Installation {

  val initial = new Installation("initial", 0, 0, 3)
  val factory = new Installation("factory", 25, 4, 3)
  val pit = new Installation("pit", 20, 5, 3)
  val attack = new Installation("attack", 25, 5, 3)
  val shield = new Installation("shield", 25, 6, 3)
  val bridge = new Installation("bridge", 10, 4, 3)
  val house = new Installation("house", 10, 4, 3)
  val town = new Installation("town", 10, 9, 3)

  val buildables = List(factory, pit, attack, shield, bridge, house, town)
}

class Installation(val name: String, val robotCost: Int, val materialCost: Int, val score: Int) {
  def head = name.head

  override def toString = name
}
