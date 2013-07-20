package net.aicomp.terraforming.entity

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope
import java.util.Random

class PlayerSpec extends SpecificationWithJUnit {
  "Player(1) == Player(1)" in {
    Player(1) must_== Player(1)
  }

  "Player(1, 'abc') != new Player(1, 'def')" in {
    Player(1, "abc") must_!= new Player(1, "def")
  }

  "Changing name values" in {
    val p1 = Player(1)
    val p2 = Player(1)
    p1 must_== p2
    p1.name = "abc"
    p2.name = "def"
    p1 must_!= p2
  }
}
