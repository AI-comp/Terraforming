package net.aicomp.entity

import org.specs2.mutable._
import org.specs2.specification.Scope

class GameSpec extends SpecificationWithJUnit {
  class games(val turn: Int = 12) extends Scope {
    val players = Vector(new Player("a", 1), new Player("b", 2),
      new Player("c", 3))
    val radius = 7
    val field = Field(radius, players.toList)
    val game = new Game(field, players.toList, turn)

    def initTile(field: Field, p: Point, player: Player) {
      field(p).owner = Some(player)
      field(p).robots = 50
      field(p).movedRobots = 0
      field(p).installation = None
      field(p).isHole = false
    }
  }

  "Game" should {
    "change turn after FinishCommand" in new games {
      game.currentTurn must_== 0
      game.currentPlayerIndex must_== 0
      game.acceptCommand(FinishCommand())
      game.currentTurn must_== 1
      game.currentPlayerIndex must_== 1
      game.acceptCommand(FinishCommand())
      game.currentTurn must_== 2
      game.currentPlayerIndex must_== 2
      game.acceptCommand(FinishCommand())
      game.currentTurn must_== 3
      game.currentPlayerIndex must_== 0
    }
    "be finished after maxTurn" in new games(1) {
      game.acceptCommand(FinishCommand())
      game.isFinished must_== true
    }
    "decline to accept build after move" in new games {
      initTile(field, Point(0, 0), players(0))
      game.acceptCommand(MoveCommand(Point(0, 0), Direction.r, 1)) must_== ()
      game.acceptCommand(BuildCommand(Point(0, 0), Installation.shield)) must
        throwA[CommandException]
    }
    "decline to accept move after build" in new games {
      initTile(field, Point(-1, 0), players(0))
      initTile(field, Point(0, -1), players(0))
      initTile(field, Point(0, 0), players(0))
      initTile(field, Point(1, 0), players(0))
      initTile(field, Point(0, 1), players(0))

      game.acceptCommand(BuildCommand(Point(0, 0), Installation.park)) must_== ()
      game.acceptCommand(MoveCommand(Point(0, 0), Direction.r, 1)) must
        throwA[CommandException]
    }
    "decline to accept build inisial" in new games {
      val origin = Point(0, 0)
      initTile(field, origin, players(0))
      val aroundPoints = Direction.all.map(_.p + origin).filter(_.within(radius))
      for (p <- aroundPoints) {
        initTile(field, p, players(0))
      }

      game.acceptCommand(BuildCommand(Point(0, 0), Installation.initial)) must throwA[CommandException]
    }
    "decline to accept build after build" in new games {
      initTile(field, Point(-1, 0), players(0))
      initTile(field, Point(0, -1), players(0))
      initTile(field, Point(0, 0), players(0))
      initTile(field, Point(1, 0), players(0))
      initTile(field, Point(0, 1), players(0))
      initTile(field, Point(0, 2), players(0))
      initTile(field, Point(1, 2), players(0))
      initTile(field, Point(2, 2), players(0))
      field(0, 0).robots = 25
      game.acceptCommand(BuildCommand(Point(0, 0), Installation.park)) must_== ()
      game.acceptCommand(BuildCommand(Point(0, 1), Installation.park)) must
        throwA[CommandException]
    }
    "stringify itself" in new games {
      game.stringify must_== "turn 0\n" +
        players(0).stringify + "\n" +
        players(1).stringify + "\n" +
        players(2).stringify + "\n" +
        field.stringify
    }
  }
}
