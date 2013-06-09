package net.aicomp.entity

class Game(val field: Field, val players: List[Player]) {
  def processCommand(player: Player, command: Command) {
    command match {
      case MoveCommand(p, dir, amount) => field.moveSquad(p, dir, amount)
      case BuildCommand(p, t) => field.build(player, p, t)
      // TODO: finish command
      case FinishCommand() => field.clearMovedRobots()
    }
  }
}
