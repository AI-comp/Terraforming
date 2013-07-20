package net.aicomp.terraforming.entity

import org.scalacheck.Gen
import org.scalacheck.Gen.choose
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop.propBoolean
import org.scalacheck.Properties
import java.util.Random

object InstallationProp extends Properties("Installation") {
  val fieldSize = 7
  val field = Field(fieldSize)
  val players = Vector(Player(0), Player(1), Player(2))
}

