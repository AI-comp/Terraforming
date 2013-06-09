package net.aicomp.entity

object Installation {
  val initial = new Installation("initial", 0)
  val br = new Installation("br", 1)
  val sh = new Installation("sh", 2)
  val at = new Installation("at", 3)
  val mt = new Installation("mt", 4)
  val pk = new Installation("pk", 5)
  val sq = new Installation("sq", 6)
  val pl = new Installation("pl", 7)

  val buildables = List(br, sh, at, mt, pk, sq, pl)
}

@cloneable class Installation(val name: String, val cost: Int) {
  def head = name.head

  override def toString = name

  override def equals(obj: Any) = obj match {
    case x: Installation => equals(x)
    case _ => false
  }

  def equals(that: Installation) = this.name == that.name
}
