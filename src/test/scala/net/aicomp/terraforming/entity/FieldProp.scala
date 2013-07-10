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
  val players = Vector(new Player(0), new Player(1), new Player(2))

  property("Field7") = forAll { (seed: Int) =>
    val f = Field(fieldSize, players, new Random(seed))
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
  
    
  
  

}
