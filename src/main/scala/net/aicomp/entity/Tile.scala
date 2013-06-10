package net.aicomp.entity

@cloneable class Tile {
  var owner: Option[Player] = None
  var robots = 0
  var movedRobots = 0
  var installation: Option[Installation] = None
  var isHole: Boolean = false

  def availableRobots = robots - movedRobots

  def ownedBy(p: Player) = owner.exists(_ == p)

  def isMovable(player: Player) = !ownedBy(player) || installation.isEmpty

  def checkLeave(player: Player, amount: Int) {
    if (isHole) {
      throw new CommandException("Robots are not able to move from a hole.")
    }
    if (availableRobots < amount) {
      throw new CommandException("The number of moving robots should be less than or equal to the number of existing movable robots.")
    }
    if (!ownedBy(player)) {
      throw new CommandException("A player cannot move from a wasted land")
    }
  }

  def leave(player: Player, amount: Int) {
    checkLeave(player, amount) // defensive
    robots -= amount
  }

  def checkEnter(player: Player, amount: Int) {
    if (!isMovable(player)) {
      throw new CommandException("A player cannot move to an other player's developed land")
    }
  }

  def enter(player: Player, amount: Int) {
    checkEnter(player, amount) // defensive
    if (owner.isEmpty) {
      // wasted land
      owner = Some(player)
      robots = amount
      movedRobots = amount
    }
    else if (ownedBy(player)) {
      // undeveloped land or developed land of the player
      robots += amount
      movedRobots += amount
    }
    else {
      // battle
      if (amount <= robots) {
        // losing or draw
        robots -= amount
      }
      else {
        // winning
        owner = Some(player)
        robots = amount - robots
        movedRobots = robots
      }
    }
  }

  override def clone() = super.clone().asInstanceOf[Tile]

  override def equals(t: Any) = t match {
    case t: Tile => equals(t)
    case _ => false
  }

  def equals(t: Tile) = owner == t.owner && robots == t.robots &&
    movedRobots == t.movedRobots && installation == t.installation && isHole == t.isHole
}
