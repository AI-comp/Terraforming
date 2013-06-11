package net.aicomp.entity

import org.specs2.mutable._

class GameSpec extends SpecificationWithJUnit {
  def initTile(field: Field, p: Point, player: Player) {
    field(p).owner = Some(player)
    field(p).robots = 10
    field(p).movedRobots = 0
    field(p).installation = None
    field(p).isHole = false
  }

  "Game" should {
    "change turn after FinishCommand" in {
      val players = Vector(new Player("a"), new Player("b"), new Player("c"))
      val game = new Game(Field(3, players.toList), players.toList, 9)
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
    "be finished after maxTurn" in {
      val players = Vector(new Player("a"), new Player("b"), new Player("c"))
      val game = new Game(Field(3, players.toList), players.toList, 1)
      game.acceptCommand(FinishCommand())
      game.isFinished must_== true
    }
    "decline to accept build after move" in {
      val players = Vector(new Player("a"), new Player("b"), new Player("c"))
      val field = Field(3, players.toList)
      initTile(field, Point(0, 0), players(0))
      val game = new Game(field, players.toList, 12)
      game.acceptCommand(MoveCommand(Point(0, 0), Direction.r, 1)) must_== ()
      game.acceptCommand(BuildCommand(Point(0, 0), Installation.shield)) must
        throwA[CommandException]
    }
    "decline to accept move after build" in {
      val players = Vector(new Player("a"), new Player("b"), new Player("c"))
      val field = Field(3, players.toList)
      initTile(field, Point(0, 0), players(0))
      val game = new Game(field, players.toList, 12)
      game.acceptCommand(BuildCommand(Point(0, 0), Installation.shield)) must_== ()
      game.acceptCommand(MoveCommand(Point(0, 0), Direction.r, 1)) must
        throwA[CommandException]
    }
    "decline to accept build after build" in {
      val players = Vector(new Player("a"), new Player("b"), new Player("c"))
      val field = Field(3, players.toList)
      initTile(field, Point(0, 0), players(0))
      initTile(field, Point(0, 1), players(0))
      val game = new Game(field, players.toList, 12)
      game.acceptCommand(BuildCommand(Point(0, 0), Installation.shield)) must_== ()
      game.acceptCommand(BuildCommand(Point(0, 1), Installation.shield)) must
        throwA[CommandException]
    }
  }
}
