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
      Point(0, 1).distance(Point(3, -1)) must_== 3
      // y
      Point(1, -2).distance(Point(-1, 2)) must_== 4
      // x + y
      Point(1, 2).distance(Point(0, 0)) must_== 3
      // 0
      Point(-1, 3).distance(Point(-1, 3)) must_== 0
    }
    "determine within distance or not" in {
      Point(0, 1).within(1) must_== true
      Point(3, -2).within(2) must_== false
      Point(3, -2).within(3) must_== true
    }
    "return shortest path to" in {
      // TODO: map with obstacles
      val field = new Field(7)
      Point(0, 1).shortestPathTo(Point(0, 1), field) must be equalTo (
        Some(List.empty[Direction]))
      Point(0, 1).shortestPathTo(Point(0, 3), field) must be equalTo (
        Some(List(Direction.dr, Direction.dr)))
      Point(2, -1).shortestPathTo(Point(0, 2), field).get.length must_==
        Point(2, -1).distance(Point(0, 2))
      Point(0, -1).shortestPathTo(Point(8, 0), field) must_== None
      Point(90, -1).shortestPathTo(Point(0, 0), field) must_== None
    }
    "return points within distance" in {
      Point.pointsWithin(1) must be equalTo (List(
        Point(-1, 0),
        Point(-1, 1),
        Point(0, -1),
        Point(0, 0),
        Point(0, 1),
        Point(1, -1),
        Point(1, 0)))
      Point.pointsWithin(-1) must be equalTo (List())
    }
  }
}
