package net.aicomp.entity

import org.specs2.mutable._

class FieldSpec extends SpecificationWithJUnit {
  "Field" should {
    "return generated field with initial positions" in {
      val player1 = new Player("a")
      val player2 = new Player("b")
      val player3 = new Player("c")
      val field = Field(7, List(player1, player2, player3))
      def filterByPlayer(player: Player) = field.points.filter(
        p => field(p).installation match {
          case Some("initial") => field(p).owner == Some(player)
          case _ => false
        })
      filterByPlayer(player1).length must_== 1
      filterByPlayer(player2).length must_== 1
      filterByPlayer(player3).length must_== 1
      field.points.foreach(p => field(p).installation match {
        case Some("initial") => ()
        case _ => field(p) must_== field(p.rotate120)
      })
    }
  }
}
