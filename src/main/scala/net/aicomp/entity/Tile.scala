package net.aicomp.entity

@cloneable class Tile {
  var owner: Option[Player] = None
  var robots = 0
  var movedRobots = 0
  var installation: Option[Installation] = None
  var isHole: Boolean = false

  def availableRobots = robots - movedRobots

  def isMovable(player: Player) = !owner.exists(_ == player) || installation.isEmpty

  override def clone() = super.clone().asInstanceOf[Tile]

  override def equals(t: Any) = t match {
    case t: Tile => equals(t)
    case _ => false
  }

  def equals(t: Tile) = owner == t.owner && robots == t.robots &&
    movedRobots == t.movedRobots && installation == t.installation && isHole == t.isHole
}
