package net.aicomp.terraforming.entity

object Installation {

  val initial = new Installation("initial", 0, 0, 3)
  val robotmaker = new Installation("robotmaker", 30, 4, 3)
  val excavator = new Installation("excavator", 20, 4, 3)
  val attack = new Installation("attack", 25, 5, 3)
//  val shelter = new Installation("shelter", 25, 6, 3)
  val bridge = new Installation("bridge", 10, 4, 3)
  val house = new Installation("house", 10, 4, 3)
  val town = new Installation("town", 10, 9, 3)

  val buildables = List(robotmaker, excavator, attack, bridge, house, town)
}

class Installation(val name: String, val robotCost: Int, val materialCost: Int, val score: Int) {
  def head = name.head

  override def toString = name
}
