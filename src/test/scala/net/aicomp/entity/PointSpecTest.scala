package net.aicomp.entity

import org.specs._
import org.specs.matcher._
import org.specs.runner.{ JUnitSuiteRunner, JUnit }
import org.junit.runner.RunWith

@RunWith(classOf[JUnitSuiteRunner])
class PointSpecTest extends Specification with JUnit {
  "Point" should {
    "calculate distance between 2 points" in {
      // x
      new Point(0, 1).distance(new Point(3, -1)) must_== 3
      // y
      new Point(1, -2).distance(new Point(-1, 2)) must_== 4
      // x + y
      new Point(1, 2).distance(new Point(0, 0)) must_== 3
      // 0
      new Point(-1, 3).distance(new Point(-1, 3)) must_== 0
    }
    "determine within distance or not" in {
      new Point(0, 1).within(1) must_== true
      new Point(3, -2).within(2) must_== false
      new Point(3, -2).within(3) must_== true
    }
    "return shortest path to" in {
      // TODO: map with obstacles
      val map = new Map(7)
      new Point(0, 1).shortestPathTo(new Point(0, 1), map) must be equalTo(
          Some(List.empty[Direction]))
      new Point(0, 1).shortestPathTo(new Point(0, 3), map) must be equalTo(
          Some(List(Direction.dr, Direction.dr)))
      new Point(2, -1).shortestPathTo(new Point(0, 2), map).get.length must_==
          new Point(2, -1).distance(new Point(0, 2))
      new Point(0, -1).shortestPathTo(new Point(8, 0), map) must_== None
      new Point(90, -1).shortestPathTo(new Point(0, 0), map) must_== None
    }
    "return points within distance" in {
      Point.pointsWithin(1) must be equalTo(List(
          new Point(-1, 0),
          new Point(-1, 1),
          new Point(0, -1),
          new Point(0, 0),
          new Point(0, 1),
          new Point(1, -1),
          new Point(1, 0)
      ))
      Point.pointsWithin(-1) must be equalTo(List())
    }
  }
}
