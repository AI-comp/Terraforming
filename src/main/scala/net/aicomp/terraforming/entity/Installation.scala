package net.aicomp.terraforming.entity

object Installation {

  val initial = new Installation("initial", 0, 0, 2)
  val factory = new Installation("factory", 25, 4, 2)
  val pit = new Installation("pit", 20, 4, 2)
  val attack = new Installation("attack", 25, 5, 2)
  val shield = new Installation("shield", 25, 6, 2)
  val bridge = new Installation("bridge", 10, 4, 2)
  val house = new Installation("house", 1, 4, 3)
  val town = new Installation("town", 1, 9, 3)
  val city = new Installation("city", 1, 19, 10)

  val buildables = List(factory, pit, attack, shield, bridge,house, town, city)
}

class Installation(val name: String, val robotCost: Int, val materialCost: Int, val score: Int) {
  def head = name.head

  override def toString = name
}
