package net.aicomp.entity

import org.scalacheck.Properties
import org.scalacheck.Prop._
import org.scalacheck.Arbitrary
import org.scalacheck.Gen

object PointProp extends Properties("Point") {
  val field = new Field(7)

  property("shortestPathTo") = forAll { (p1: Point, p2: Point) =>
    if (p1.within(field.radius) && p2.within(field.radius))
      p1.shortestPathTo(p2, field).get.size == p1.distance(p2)
    else
      p1.shortestPathTo(p2, field) == None
  }

  implicit def arbPoint: Arbitrary[Point] =
    Arbitrary {
      for {
        x <- Gen.choose(-field.radius, field.radius)
        y <- Gen.choose(-field.radius, field.radius)
      } yield Point(x, y)
    }
}