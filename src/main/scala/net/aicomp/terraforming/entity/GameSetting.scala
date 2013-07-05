package net.aicomp.terraforming.entity

/**
 * @constructor
 * @param initialMaterials the quantity of material owned by players when starting the game.
 */
@SerialVersionUID(0l)
case class GameSetting(val maxRound: Int = 200) {
}

object GameSetting {
  val defaultInstance = GameSetting()
}
