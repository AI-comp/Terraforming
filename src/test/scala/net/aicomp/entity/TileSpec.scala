package net.aicomp.entity

import org.specs2.mutable._

class TileSpec extends SpecificationWithJUnit {
  "Tile" should {
    "equals another tile which has same values" in {
      new Tile().equals(new Tile()) must_== true
    }
    "not equals another tile which has other values" in {
      val tile = new Tile()
      tile.isHole = true
      tile.equals(new Tile()) must_== false
    }
    "not equals another object" in {
      new Tile().equals(new Object()) must_== false
    }
  }
}