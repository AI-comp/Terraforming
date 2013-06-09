package net.aicomp.entity

object Installation {

  // TODO: rename
  val initial = new Installation("initial")
  val br = new Installation("bridge")
  val sh = new Installation("shield")
  val at = new Installation("attack")
  val mt = new Installation("material")
  val pk = new Installation("park")
  val sq = new Installation("square")
  val pl = new Installation("public")

  val buildables = List(br, sh, at, mt, pk, sq, pl)
}

class Installation(val name: String) {
  def head = name.head

  override def toString = name
}
