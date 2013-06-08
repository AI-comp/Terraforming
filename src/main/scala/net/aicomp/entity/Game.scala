package net.aicomp.entity

class Game(val field: Field, val players: List[Player]) {
  def processCommand(player: Player, command: Command) {
    command match {
      case MoveCommand(p, dir) => field.moveSquad(p, dir)
      case BuildCommand(p, t) => field.build(p, t)
      // TODO: finish command
      case FinishCommand() => ()
    }
  }
}
