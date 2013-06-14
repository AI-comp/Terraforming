package net.aicomp.entity

import org.specs2.mutable._
import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

class PointSpec extends SpecificationWithJUnit {
  "Point" should {
    "calculate additions" in {
      Point(1, 1) + Point(2, -1) must_== Point(3, 0)
    }
    "calculate subtractions" in {
      Point(1, 3) - Point(-1, -1) must_== Point(2, 4)
    }
    "calculate with directions" in {
      Point(2, 2) + Direction.ur must_== Point(3, 1)
      Point(2, 2) - Direction.ul must_== Point(2, 3)
    }
    "calculate multiplication" in {
      Point(2, 1) * 3 must_== Point(6, 3)
    }
    "calculate rotated point" in {
      Point(1, 0).rotate120 must_== Point(-1, 1)
      Point(-2, 1).rotate120 must_== Point(1, -2)
      Point(-3, -2).rotate120.rotate240 must_== Point(-3, -2)
    }
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
      val field = Field(7)
      val player = new Player(1)
      val enemy = new Player(2)
      Point(0, 1).shortestPathTo(Point(0, 1), field, player) must_==
        Some(List.empty[Direction])
      Point(0, 1).shortestPathTo(Point(0, 3), field, player) must_==
        Some(List(Direction.dr, Direction.dr))
      Point(2, -1).shortestPathTo(Point(0, 2), field, player).get.length must_==
        Point(2, -1).distance(Point(0, 2))
      Point(0, -1).shortestPathTo(Point(8, 0), field, player) must_== None
      Point(90, -1).shortestPathTo(Point(0, 0), field, player) must_== None

      def obstacle(t: Tile) {
        t.owner = Some(enemy)
        t.robots = 1000000000
        t.installation = Some(Installation.bridge)
      }

      val fieldWithObstacle = Field(2)
      obstacle(fieldWithObstacle(0, 0))
      obstacle(fieldWithObstacle(-1, 0))
      Point(0, 1).shortestPathTo(Point(0, -1), fieldWithObstacle, player) must_==
        Some(List(Direction.ur, Direction.ul, Direction.l))
      Point(0, 0).shortestPathTo(Point(0, -1), fieldWithObstacle, player) must_== None

      val fieldWithWall = Field(1)
      obstacle(fieldWithWall(-1, 0))
      obstacle(fieldWithWall(0, 0))
      obstacle(fieldWithWall(1, 0))
      Point(0, 1).shortestPathTo(Point(0, -1), fieldWithWall, player) must_== None
      Point(-1, 1).shortestPathTo(Point(0, 1), fieldWithWall, player) must_==
        Some(List(Direction.r))
    }
    "stringify itself" in {
      Point(1, 2).stringify must_== "1 2"
    }
    "return points within distance" in {
      Point.pointsWithin(1) must_== List(
        Point(-1, 0),
        Point(-1, 1),
        Point(0, -1),
        Point(0, 0),
        Point(0, 1),
        Point(1, -1),
        Point(1, 0))
      Point.pointsWithin(-1) must_== Nil
    }
  }
}
