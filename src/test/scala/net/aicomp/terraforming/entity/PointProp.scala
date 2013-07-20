package net.aicomp.terraforming.entity

import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop.propBoolean
import org.scalacheck.Properties

object PointProp extends Properties("Point") {
  val field = Field(7)
  val player = Player(0)

  property("shortestPathTo") = forAll(pointWithin, pointWithin) { (p1: Point, p2: Point) =>
    p1.shortestPathTo(p2, field, player).get.size == p1.distance(p2)
  }

  property("shortestPathTo") = forAll(pointWithin | pointWithout, pointWithout) { (p1: Point, p2: Point) =>
    p1.shortestPathTo(p2, field, player) == None
  }

  property("shortestPathTo") = forAll(pointWithout, pointWithin | pointWithout) { (p1: Point, p2: Point) =>
    p1.shortestPathTo(p2, field, player) == None
  }

  property("shortestPathTo") = forAll(pointWithout, pointWithout) { (p1: Point, p2: Point) =>
    p1.shortestPathTo(p2, field, player) == None
  }

  def pointWithin = (for {
    x <- Gen.choose(-field.radius, field.radius)
    y <- Gen.choose(-field.radius, field.radius)
  } yield Point(x, y)).filter(p => p.within(field.radius))

  def pointWithout = (for {
    x <- Gen.choose(-field.radius * 2, field.radius * 2)
    y <- Gen.choose(-field.radius * 2, field.radius * 2)
  } yield Point(x, y)).filter(p => !p.within(field.radius))
}
