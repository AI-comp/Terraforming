package net.aicomp.terraforming.entity

class Tile extends Cloneable {
  var owner: Option[Player] = None
  var robots = 0
  var movedRobots = 0
  var additionalScore = 0
  var aroundShelter = 0
  var installation: Option[Installation] = None
  var isHole: Boolean = false

  def copy() = clone()

  def availableRobots = robots - movedRobots

  def ownedBy(p: Player) = owner.exists(_ == p)

  def existBaseMaterialOf(p: Player) = owner.exists(_ == p) && installation.isEmpty && !isHole

  def isMovable(player: Player) = ownedBy(player) || installation.isEmpty

  def checkLeave(player: Player, amount: Int) {
    if (isHole) {
      throw new CommandException("Robots are not able to move from a hole.")
    }
    if (availableRobots < amount) {
      throw new CommandException("The number of moving robots should be less than or equal to the number of existing movable robots.")
    }
    if (!ownedBy(player)) {
      throw new CommandException("A player cannot move from a waste land")
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
    if (ownedBy(player)) {
      // developed land of the player
      robots += amount
      movedRobots += amount
    } else {
      // battle (with waste land's robots or enemy robots)
      if (amount <= robots) {
        // losing or draw
        robots -= amount
      } else {
        // winning
        owner = Some(player)
        robots = amount - robots
        movedRobots = robots
      }
    }
  }

  def score(player: Player): Int = {
    val wasteLand = 0
    val undevelopedLand = 1
    val developedLand = 3
    val hole = 0
    if (ownedBy(player)) {
      if (isHole) hole
      else installation match {
        case Some(ins) => ins.score + additionalScore
        case None => undevelopedLand
      }
    } else wasteLand
  }

  def landform(): String = {
    if (isHole) "hole"
    else {
      owner match {
        case Some(player) => installation match {
          case Some(ins) => "base"
          case None => "settlement"
        }
        case None => "wasteland"
      }
    }
  }

  def toJsonMap(materialAmount: Int) = {
    (owner match {
      case Some(player) => Map("id" -> player.id)
      case None => Map()
    }) ++
      Map("l" -> landform.substring(0, 1)) ++
      (if (robots > 0) { Map("r" -> robots) } else { Map() }) ++
      (if (materialAmount > 0) { Map("m" -> materialAmount) } else { Map() }) ++
      (installation match {
        case Some(buildings) => Map("i" -> buildings.toString.substring(0, 2))
        case None => Map()
      })
  }

  def stringify(materialAmount: Int) = {
    // "owner_id robots object"
    val owner_id = owner match {
      case Some(player) => player.id
      case None => -1
    }
    val obj = if (isHole) "hole" else installation match {
      case Some(b) => b.toString
      case None => "none"
    }
    owner_id + " " + robots + " " + materialAmount + " " + landform + " " + obj
  }

  override def clone() = super.clone().asInstanceOf[Tile]

  override def equals(t: Any) = t match {
    case t: Tile => equals(t)
    case _ => false
  }

  def equals(t: Tile) = owner == t.owner && robots == t.robots &&
    movedRobots == t.movedRobots && installation == t.installation && isHole == t.isHole
}
