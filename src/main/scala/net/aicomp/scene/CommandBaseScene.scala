package net.aicomp.scene

import jp.ac.waseda.cs.washi.gameaiarena.gui.Scene
import net.aicomp.entity.Game
import net.aicomp.entity.GameEnvironment

abstract class CommandBaseScene extends AbstractScene {
  override def execute() = {
    nextCommand match {
      case Some(command) =>
        displayLine("> " + command.mkString(" "))
        if (command.length > 0) execute(command) else (game, this)
      case None =>
        (game, this)
    }
  }

  def nextCommand: Option[List[String]]

  def execute(words: List[String]): (Game, Scene[GameEnvironment])
}
