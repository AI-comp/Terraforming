package net.aicomp.terraforming.entity

import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop.propBoolean
import org.scalacheck.Properties
import java.util.Random

object FieldProp extends Properties("Field") {
  val field = Field(7)
  val players = Vector(new Player(0), new Player(1), new Player(2))

  property("CalculateScore for Filed whose size is 7") = forAll { (seed: Int) =>
    val f = Field(7, players, new Random(seed))
    f.calculateScore(players(0)) == f.calculateScore(players(1)) &&
      f.calculateScore(players(1)) == f.calculateScore(players(2))
  }

  property("Build installations") = forAll { (seed: Int) =>
    val r = new Random(seed)
    for (size <- 1 to 7) {
      val f = Field(size, players, r)
      for ((p, tile) <- field.tiles) {
        tile.owner = Some(players(r.nextInt(3)))
        tile.robots = 1000
      }
      for ((p, tile) <- field.tiles) {
        val inst = Installation.buildables(r.nextInt(Installation.buildables.size))
        try {
          f.build(tile.owner.get, p, inst)
        } catch {
          case CommandException(msg) => ()
        }
      }
    }
    true
  }

  property("Build pits, houses and towns") = forAll { (seed: Int) =>
    val r = new Random(seed)
    val builables = Vector(Installation.pit, Installation.house, Installation.town)
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
