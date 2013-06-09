package net.aicomp.entity

import org.specs2.mutable._

class FieldSpec extends SpecificationWithJUnit {
  def initTile(field: Field, p: Point) {
    field(p).owner = None
    field(p).robots = 0
    field(p).movedRobots = 0
    field(p).installation = None
    field(p).isHole = false
  }
  "Field" should {
    "return generated field with initial positions" in {
      val player1 = new Player("a")
      val player2 = new Player("b")
      val player3 = new Player("c")
      val field = Field(7, List(player1, player2, player3))
      def filterByPlayer(player: Player) = field.points.filter(
        p => field(p).installation match {
          case Some(_) => field(p).owner.exists(_ == player)
          case _ => false
        })
      filterByPlayer(player1).size must_== 1
      filterByPlayer(player2).size must_== 1
      filterByPlayer(player3).size must_== 1
      field.points.foreach(p => {
        var copy = field(p).clone
        if (copy.installation.exists(_ == Installation.initial)) {
          copy.owner = field(p.rotate120).owner
        }
        copy must_== field(p.rotate120)
      })
    }
    "allow to moveSquad into a hole" in {
      val players = Vector(new Player("a"), new Player("b"), new Player("c"))
      val field = Field(3, players.toList)
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(0))
      field(0, 0).robots = 10
      field(0, 0).isHole = false
      initTile(field, Point(1, 0))
      field(1, 0).isHole = true
      field.moveSquad(Point(0, 0), Direction.r, 4) must_== ()
    }
    "decline to moveSquad from a hole" in {
      val players = Vector(new Player("a"), new Player("b"), new Player("c"))
      val field = Field(3, players.toList)
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(0))
      field(0, 0).robots = 10
      field(0, 0).isHole = true
      initTile(field, Point(1, 0))
      field(1, 0).isHole = false
      field.moveSquad(Point(0, 0), Direction.r, 4) must throwA[CommandException]
    }
    "remove hole when bridge is built" in {
      val players = Vector(new Player("a"), new Player("b"), new Player("c"))
      val field = Field(3, players.toList)
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(0))
      field(0, 0).robots = 10
      field(0, 0).isHole = true
      field.build(players(0), Point(0, 0), Installation.br) must_== ()
      field(0, 0).isHole must_== false
    }
    "decline to build installations othe than bridge on a hole" in {
      val players = Vector(new Player("a"), new Player("b"), new Player("c"))
      val field = Field(3, players.toList)
      initTile(field, Point(0, 0))
      field(0, 0).owner = Some(players(0))
      field(0, 0).robots = 10
      field(0, 0).isHole = true
      field.build(players(0), Point(0, 0), Installation.sh) must
        throwA[CommandException]
      field.build(players(0), Point(0, 0), Installation.at) must
        throwA[CommandException]
    }
  }
}
