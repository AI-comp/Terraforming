package net.aicomp.terraforming.entity

object Installation {

  val initial = new Installation("initial", 0, 0, 3)
  val robotmaker = new Installation("robotmaker", 50, 4, 3)
  val excavator = new Installation("excavator", 20, 4, 3)
  val tower = new Installation("tower", 25, 5, 3)
  val bridge = new Installation("bridge", 15, 4, 3)
  val house = new Installation("house", 10, 4, 3)
  val town = new Installation("town", 10, 9, 3)

  val buildables = List(robotmaker, excavator, tower, bridge, house, town)
}

class Installation(val name: String, val robotCost: Int, val materialCost: Int, val score: Int) {
  def head = name.head

  override def toString = name
}
