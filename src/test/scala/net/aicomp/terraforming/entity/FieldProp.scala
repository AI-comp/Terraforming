package net.aicomp.terraforming.entity

import org.scalacheck.Gen
import org.scalacheck.Gen.choose
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop.propBoolean
import org.scalacheck.Properties
import java.util.Random

object FieldProp extends Properties("Field") {
  val fieldSize = 7
  val field = Field(fieldSize)
  val players = Vector(Player(0), Player(1), Player(2))

  property("CalculateScore for Filed whose size is 7") =
    forAll { (seed: Int) =>
      val random = new Random(seed)
      val f = Field(fieldSize, players, random)
      f.calculateScore(players(0)) == f.calculateScore(players(1)) &&
        f.calculateScore(players(1)) == f.calculateScore(players(2))
    }

  property("avairableAroundPoints") =
    forAll(choose(-6, 6), choose(-6, 6), choose(1, 6)) {
      (x: Int, y: Int, randSize: Int) =>
        (-fieldSize < x + y && x + y < fieldSize) ==> {
          val p = Point(x, y)
          val pointsSize = field.availableAroundPoints(p, randSize).size
          4 <= pointsSize
        }
    }

  property("avairableAroundTiles") =
    forAll(choose(-6, 6), choose(-6, 6), choose(1, 6)) {
      (x: Int, y: Int, randSize: Int) =>
        (-fieldSize < x + y && x + y < fieldSize) ==> {
          val p = Point(x, y)
          field.availableAroundTiles(p, randSize).size >= 4
        }
    }

  property("Build installations") =
    forAll { (seed: Int) =>
      val random = new Random(seed)

      for (size <- 1 to 7) {
        val f = Field(size, players, random)
        for ((p, tile) <- field.tiles) {
          tile.owner = Some(players(random.nextInt(3)))
          tile.robots = 1000
        }
        for ((p, tile) <- field.tiles) {
          val inst = Installation.buildables(random.nextInt(Installation.buildables.size))
          try {
            f.build(tile.owner.get, p, inst)
          } catch {
            case CommandException(msg) => ()
          }
        }
      }
      true
    }

  property("Build pits, houses and towns") =
    forAll { (seed: Int) =>
      val r = new Random(seed)
      val builables = Vector(Installation.excavator, Installation.house, Installation.town)
      for (size <- 1 to 7) {
        val f = Field(size, players, r)
        for ((p, tile) <- field.tiles) {
          tile.owner = Some(players(r.nextInt(3)))
          tile.robots = 1000
        }
        for ((p, tile) <- field.tiles) {
          val inst = builables(r.nextInt(builables.size))
          try {
            f.build(tile.owner.get, p, inst)
          } catch {
            case CommandException(msg) => ()
          }
        }
      }
      true
    }
}
