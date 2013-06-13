package net.aicomp.entity

import org.specs2.mutable._
import org.specs2.specification.Scope

class GameSpec extends SpecificationWithJUnit {
  class games(val turn: Int = 12) extends Scope {
    val players = Vector(new Player("a", 1), new Player("b", 2),
        new Player("c", 3))
    val field = Field(7, players.toList)
    val game = new Game(field, players.toList, turn)

    def initTile(field: Field, p: Point, player: Player) {
      field(p).owner = Some(player)
      field(p).robots = 10
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
      initTile(field, Point(0, 0), players(0))
      game.acceptCommand(BuildCommand(Point(0, 0), Installation.shield)) must_== ()
      game.acceptCommand(MoveCommand(Point(0, 0), Direction.r, 1)) must
        throwA[CommandException]
    }
    "decline to accept build after build" in new games {
      initTile(field, Point(0, 0), players(0))
      initTile(field, Point(0, 1), players(0))
      game.acceptCommand(BuildCommand(Point(0, 0), Installation.shield)) must_== ()
      game.acceptCommand(BuildCommand(Point(0, 1), Installation.shield)) must
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
