package net.aicomp.terraforming.entity

import org.specs2.mutable.SpecificationWithJUnit

class TileSpec extends SpecificationWithJUnit {
  "Tile" should {
    val holeScore = 0
    val wasteLandScore = 0
    val undevelopedLandScore = 1
    val developedLandScore = 2
    val houseFacilityScore = 3
    val cityFacilityScore = 10
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
    "score a waste land" in {
      val player = new Player(1)
      new Tile().score(player) must_== wasteLandScore
    }
    "score a hole" in {
      val player = new Player(1)
      val tile = new Tile
      tile.owner = Some(player)
      tile.isHole = true
      tile.score(player) must_== holeScore
    }
    "score a undeveloped land" in {
      val player = new Player(1)
      val tile = new Tile
      tile.owner = Some(player)
      tile.score(player) must_== undevelopedLandScore
    }
    "score a developed land" in {
      val player = new Player(1)
      val tile = new Tile
      tile.owner = Some(player)
      tile.installation = Some(Installation.attack)
      tile.score(player) must_== developedLandScore
    }
    "score a developed land in which a city facility built" in {
      val player = new Player(1)
      val tile = new Tile
      tile.owner = Some(player)
      tile.installation = Some(Installation.city)
      tile.score(player) must_== developedLandScore + cityFacilityScore
    }
    "stringify wasteland" in {
      val tile = new Tile
      tile.robots = 1
      tile.stringify must_== "-1 1 none"
    }
    "stringify undeveloped land" in {
      val player = new Player(1)
      val tile = new Tile
      tile.owner = Some(player)
      tile.robots = 2
      tile.stringify must_== "1 2 none"
    }
    "stringify developed land" in {
      val player = new Player(2)
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
      val player = new Player(2)
      val tile = new Tile
      tile.owner = Some(player)
      tile.robots = 3
      tile.isHole = true
      tile.stringify must_== "2 3 hole"
    }
  }
}
