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
    "stringify wasteland" in {
      val tile = new Tile
      tile.robots = 1
      tile.stringify must_== "-1 1 none"
    }
    "stringify undeveloped land" in {
      val player = new Player("a", 1)
      val tile = new Tile
      tile.owner = Some(player)
      tile.robots = 2
      tile.stringify must_== "1 2 none"
    }
    "stringify developed land" in {
      val player = new Player("b", 2)
      val tile = new Tile
      tile.owner = Some(player)
      tile.robots = 3
      tile.installation = Some(Installation.bridge)
      tile.stringify must_== "2 3 bridge"
    }
    "stringify hole" in {
      val tile = new Tile
      tile.robots = 1
      tile.isHole = true
      tile.stringify must_== "-1 1 hole"
    }
    "stringify occupied hole" in {
      val player = new Player("b", 2)
      val tile = new Tile
      tile.owner = Some(player)
      tile.robots = 3
      tile.isHole = true
      tile.stringify must_== "2 3 hole"
    }
  }
}
