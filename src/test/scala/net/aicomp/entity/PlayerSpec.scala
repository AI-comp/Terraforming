package net.aicomp.entity

import org.specs2.mutable._

class PlayerSpec extends SpecificationWithJUnit {
  "Player" should {
    "stringify itself" in {
      val player = new Player("abc", 1)
      player.stringify must_== "1 abc"
    }
  }
}
