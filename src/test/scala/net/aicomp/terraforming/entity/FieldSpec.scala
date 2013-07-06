package net.aicomp.terraforming.entity

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope

class FieldSpec extends SpecificationWithJUnit {
  trait fields extends Scope {
    val players = Vector(new Player(1), new Player(2), new Player(3))
    val radius = 7
    val field = Field(radius, players)

    def initTile(field: Field, p: Point) {
      field(p).owner = None
      field(p).robots = 0
      field(p).movedRobots = 0
      field(p).installation = None
      field(p).isHole = false
    }
  }

  "Field" should {
    "return generated field with initial positions" in new fields {
      def filterByPlayer(player: Player) = field.points.filter(
        p => field(p).installation match {
          case Some(_) => field(p).owner.exists(_ == player)
          case _ => false
        })

      filterByPlayer(players(0)).size must_== 1
      filterByPlayer(players(1)).size must_== 1
      filterByPlayer(players(2)).size must_== 1
      field.points.foreach(p => {
        var copy = field(p).clone
        if (copy.installation.exists(_ == Installation.initial)) {
          copy.owner = field(p.rotate120).owner
        }
        copy must_== field(p.rotate120)
      })
    }
    "allow to moveSquad into a hole" in new fields {
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(0))
      field(0, 0).robots = 10
      field(0, 0).isHole = false
      initTile(field, Point(1, 0))
      field(1, 0).isHole = true
      field.moveSquad(players(0), Point(0, 0), Direction.r, 4) must_== ()
    }
    "decline to moveSquad from a hole" in new fields {
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(0))
      field(0, 0).robots = 10
      field(0, 0).isHole = true
      initTile(field, Point(1, 0))
      field(1, 0).isHole = false
      field.moveSquad(players(0), Point(0, 0), Direction.r, 4) must
        throwA[CommandException]
    }
    "decline to move too many robots" in new fields {
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(0))
      field(0, 0).robots = 10
      initTile(field, Point(1, 0))
      field.moveSquad(players(0), Point(0, 0), Direction.r, 20) must
        throwA[CommandException]
    }
    "remove hole when bridge is built" in new fields {
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(0))
      field(0, -1).owner = Some(players(0))
      field(0, -1).isHole = false
      field(0, 1).owner = Some(players(0))
      field(0, 1).isHole = false
      field(1, 0).owner = Some(players(0))
      field(1, 0).isHole = false
      field(-1, 0).owner = Some(players(0))
      field(-1, 0).isHole = false
      field(0, 0).robots = 10
      field(0, 0).isHole = true
      field.build(players(0), Point(0, 0), Installation.bridge) must_== ()
      field(0, 0).isHole must_== false
    }
    "decline to build installations other than bridge on a hole" in new fields {
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(0))
      field(0, 0).robots = 10
      field(0, 0).isHole = true
      field.build(players(0), Point(0, 0), Installation.shield) must
        throwA[CommandException]
      field.build(players(0), Point(0, 0), Installation.attack) must
        throwA[CommandException]
    }
    "decline to moveSquad from unoccupied tile" in new fields {
      initTile(field, Point(0, 0))
      field.moveSquad(players(0), Point(0, 0), Direction.r, 0) must
        throwA[CommandException]
    }
    "decline to moveSquad from enemy's tile" in new fields {
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(1))
      field(0, 0).robots = 10
      field.moveSquad(players(0), Point(0, 0), Direction.r, 5) must
        throwA[CommandException]
    }
    "change the owner of tile after moveSquad to an empty tile" in new fields {
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(0))
      field(0, 0).robots = 10
      initTile(field, Point(1, 0))
      field.moveSquad(players(0), Point(0, 0), Direction.r, 4) must_== ()
      field(0, 0).robots must_== 6
      field(1, 0).owner must_== Some(players(0))
      field(1, 0).robots must_== 4
      field(1, 0).movedRobots must_== 4
    }
    "change the owner of tile after winning battle" in new fields {
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(0))
      field(0, 0).robots = 10
      initTile(field, Point(1, 0))
      field(1, 0).owner = Some(players(1))
      field(1, 0).robots = 8
      field.moveSquad(players(0), Point(0, 0), Direction.r, 10) must_== ()
      field(1, 0).owner must_== Some(players(0))
      field(1, 0).robots must_== 2
      field(1, 0).movedRobots must_== 2
    }
    "not change the owner of tile after losing battle" in new fields {
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(0))
      field(0, 0).robots = 10
      initTile(field, Point(1, 0))
      field(1, 0).owner = Some(players(1))
      field(1, 0).robots = 18
      field.moveSquad(players(0), Point(0, 0), Direction.r, 10) must_== ()
      field(1, 0).owner must_== Some(players(1))
      field(1, 0).robots must_== 8
      field(1, 0).movedRobots must_== 0
    }
    "change the owner of waste land after winning battle" in new fields {
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(0))
      field(0, 0).robots = 10
      initTile(field, Point(1, 0))
      field(1, 0).owner = None
      field(1, 0).robots = 8
      field.moveSquad(players(0), Point(0, 0), Direction.r, 10) must_== ()
      field(1, 0).owner must_== Some(players(0))
      field(1, 0).robots must_== 2
      field(1, 0).movedRobots must_== 2
    }
    "not change the owner of waste land after losing battle" in new fields {
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(0))
      field(0, 0).robots = 10
      initTile(field, Point(1, 0))
      field(1, 0).owner = None
      field(1, 0).robots = 18
      field.moveSquad(players(0), Point(0, 0), Direction.r, 10) must_== ()
      field(1, 0).owner must_== None
      field(1, 0).robots must_== 8
      field(1, 0).movedRobots must_== 0
    }
    "not change the owner of tile after draw battle" in new fields {
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(0))
      field(0, 0).robots = 10
      initTile(field, Point(1, 0))
      field(1, 0).owner = Some(players(1))
      field(1, 0).robots = 10
      field.moveSquad(players(0), Point(0, 0), Direction.r, 10) must_== ()
      field(1, 0).owner must_== Some(players(1))
      field(1, 0).robots must_== 0
      field(1, 0).movedRobots must_== 0
    }
    "allow to move robots onto one's own developed land" in new fields {
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(0))
      field(0, 0).robots = 10
      initTile(field, Point(1, 0))
      field(1, 0).owner = Some(players(0))
      field(1, 0).installation = Some(Installation.bridge)
      field.moveSquad(players(0), Point(0, 0), Direction.r, 10) must_== ()
    }
    "decline to move robots onto other player's developed land" in new fields {
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(0))
      field(0, 0).robots = 10
      initTile(field, Point(1, 0))
      field(1, 0).owner = Some(players(1))
      field(1, 0).installation = Some(Installation.bridge)
      field.moveSquad(players(0), Point(0, 0), Direction.r, 10) must
        throwA[CommandException]
    }
    "decline to choose an outer tile of the field" in new fields {
      field.moveSquad(players(0), Point(99, 99), Direction.r, 10) must
        throwA[CommandException]
      field.build(players(0), Point(99, 99), Installation.house) must
        throwA[CommandException]
    }
    "decline to move robots onto outside of the field" in new fields {
      val p = Point(radius, 0)
      initTile(field, p)
      field(p).owner = Some(players(0))
      field(p).robots = 10
      field.moveSquad(players(0), p, Direction.r, 10) must
        throwA[CommandException]
    }
    "calculate the one's materialAmount of the initilized field" in new fields {
      def filterByPlayer(player: Player) = field.points.filter(
        p => field(p).installation match {
          case Some(_) => field(p).owner.exists(_ == player)
          case _ => false
        })
      for (player <- players) {
        filterByPlayer(player).foreach(
          field.aroundMaterialAmount(_, players(0)) must_== 0)
      }
    }

    trait origin extends fields {
      val origin = Point(0, 0)
      val player = Some(players(0))
      val aroundPoints = Direction.all.map(_.p + origin).filter(_.within(radius))

      initTile(field, origin)
      field(origin).owner = player
    }

    "calculate the one's materialAmount of the own settlment surrounded own settlement" in new fields with origin {
      for (p <- aroundPoints) {
        initTile(field, p)
        field(p).owner = player
      }
      field.aroundMaterialAmount(origin, players(0)) must_== 7
    }
    "calculate the one's materialAmount of the own settlment surrounded own hole" in new fields with origin {
      for (p <- aroundPoints) {
        initTile(field, p)
        field(p).owner = Some(players(0))
        field(p).isHole = true
      }
      field.aroundMaterialAmount(origin, players(0)) must_== 1
    }
    "calculate the one's materialAmount of the own settlment surrounded own city" in new fields with origin {
      for (p <- aroundPoints) {
        initTile(field, p)
        Direction.all.map(_.p + p).filter(_.within(radius)).foreach({ _p =>
          initTile(field, _p)
          field(_p).owner = player
        })
      }
      for (p <- aroundPoints) {
        field(p).owner = player
        field(p).robots = 1
        field.build(player.get, p, Installation.house)
      }
      field.aroundMaterialAmount(origin, players(0)) must_== 1
    }
    trait originAndPit extends origin {
      val pitPlace = Point(1, 1)
      val aroundPits = Direction.all.map(_.p + pitPlace).filter(_.within(radius))
      for (p <- aroundPits) {
        initTile(field, p)
        field(p).owner = player
      }
      initTile(field, pitPlace)
      field(pitPlace).owner = player
      field(pitPlace).robots = 20
      field.build(player.get, pitPlace, Installation.pit)
    }
    "calculate the one's materialAmount of the own settlment surrounded own settlement and pit" in new fields with originAndPit {
      for (p <- aroundPoints) {
        initTile(field, p)
        field(p).owner = Some(players(0))
      }
      field.aroundMaterialAmount(origin, players(0)) must_== 9
    }
    "calculate the one's materialAmount of the own settlment surrounded own hole and pit" in new fields with originAndPit {
      for (p <- aroundPoints) {
        initTile(field, p)
        field(p).owner = Some(players(0))
        field(p).isHole = true
      }
      field.aroundMaterialAmount(origin, players(0)) must_== 1
    }
    "calculate the one's materialAmount of the own settlment surrounded own city and pit" in new fields with originAndPit {
      for (p <- aroundPoints) {
        initTile(field, p)
        Direction.all.map(_.p + p).filter(_.within(radius)).foreach({ _p =>
          if (_p != pitPlace) {
            initTile(field, _p)
            field(_p).owner = player
          }
        })
      }
      for (p <- aroundPoints) {
        field(p).owner = player
        field(p).robots = 1
        field.build(player.get, p, Installation.house)
      }
      field.aroundMaterialAmount(origin, players(0)) must_== 1
    }
    "calculate the one's own score of the initilized field" in new fields {
      val initialScore = 2
      field.calculateScore(players(0)) must_== initialScore
      field.calculateScore(players(1)) must_== initialScore
      field.calculateScore(players(2)) must_== initialScore
    }
    "calculate the one's own score of the field" in new fields {
      val initialScore = 2
      val undevelopedLandScore = 1
      val developedLandScore = 2
      val cityFacilityScore = 10
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(0))
      field.calculateScore(players(0)) must_== initialScore + undevelopedLandScore
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(0))
      field(0, 0).installation = Some(Installation.attack)
      field.calculateScore(players(0)) must_== initialScore + developedLandScore
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(0))
      field(0, 0).installation = Some(Installation.city)
      field.calculateScore(players(0)) must_== initialScore + developedLandScore + cityFacilityScore
    }
    "stringify itself" in {
      val players = Vector(new Player(1), new Player(2), new Player(3))
      val field = Field(1, players)
      field.stringify must_==
        "2 7\n" +
        "-1 0 " + field(-1, 0).stringify + "\n" +
        "-1 1 " + field(-1, 1).stringify + "\n" +
        "0 -1 " + field(0, -1).stringify + "\n" +
        "0 0 " + field(0, 0).stringify + "\n" +
        "0 1 " + field(0, 1).stringify + "\n" +
        "1 -1 " + field(1, -1).stringify + "\n" +
        "1 0 " + field(1, 0).stringify + "\n"
    }
  }
}
