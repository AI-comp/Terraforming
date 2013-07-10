package net.aicomp.terraforming.entity

import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop.propBoolean
import org.scalacheck.Properties
import java.util.Random

object FieldProp extends Properties("Field") {
  val field = Field(7)
  val players = Vector(new Player(0), new Player(1), new Player(2))

  property("Field7") = forAll { (seed: Int) =>
    val f = Field(7, players, new Random(seed))
    f.calculateScore(players(0)) == f.calculateScore(players(1)) &&
      f.calculateScore(players(1)) == f.calculateScore(players(2))
  }
}
