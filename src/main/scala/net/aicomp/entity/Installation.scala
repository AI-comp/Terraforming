package net.aicomp.entity

object Installation {
  val initial = new Installation("initial")
  val br = new Installation("br")
  val sh = new Installation("sh")
  val at = new Installation("at")
  val mt = new Installation("mt")
  val pk = new Installation("pk")
  val sq = new Installation("sq")
  val pl = new Installation("pl")

  val buildables = List(br, sh, at, mt, pk, sq, pl)
}

class Installation(val name: String) {
  def head = name.head

  override def toString = name
}
