package net.aicomp.entity

object Installation {

  // TODO: rename
  val initial = new Installation("initial", 0)
  val br = new Installation("bridge", 1)
  val sh = new Installation("shield", 2)
  val at = new Installation("attack", 3)
  val mt = new Installation("material", 4)
  val pk = new Installation("park", 5)
  val sq = new Installation("square", 6)
  val pl = new Installation("public", 7)

  val buildables = List(br, sh, at, mt, pk, sq, pl)
}

class Installation(val name: String, val cost: Int) {
  def head = name.head

  override def toString = name
}
