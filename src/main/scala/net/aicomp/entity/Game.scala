package net.aicomp.entity

class Game {
  val field = new Field(7)
  // TODO: multiple players
  val player = new Player

  def processCommand(player: Player, command: Command) {
    command match {
      case MoveCommand(p, dir) => field.moveSquad(p, dir)
      case BuildCommand(p, t) => field.build(p, t)
      // TODO: finish command
      case FinishCommand() => ()
    }
  }
}
