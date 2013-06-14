package net.aicomp.scene

import net.aicomp.entity.GameEnvironment
import net.aicomp.util.misc.Logger
import net.aicomp.util.settings.Defaults
import net.exkazuu.gameaiarena.gui.DefaultScene
import net.exkazuu.gameaiarena.gui.Scene

abstract class AbstractScene extends DefaultScene[GameEnvironment] {
  def env = getEnvironment
  def renderer = env.getRenderer
  def inputer = env.getInputer
  def game = env.game

  override def run() = {
    val commandStrings = nextCommandStrnigs
      .filter(_ != null)
      .map(_.filter(_.length > 0).toList)
      .filter(_.length > 0)
      .toList
    if (commandStrings.length > 0) {
      for (commandString <- commandStrings) {
        displayLine("> " + commandString.mkString(" "))
      }
      runWithCommands(commandStrings)
    } else {
      AbstractScene.this
    }
  }

  protected def nextCommandStrnigs = game.currentPlayer.manipulator.run(game)

  protected def runWithCommands(commandStrings: List[List[String]]): Scene[GameEnvironment]

  protected def displayCore(text: String)

  final def display(text: String) {
    displayCore(text)
    Logger.writeAndFlush(text)
  }

  final def displayLine(text: String) {
    display(text + Defaults.NEW_LINE)
  }

  final def describe(sceneDescription: String) {
    val dashes = "-" * 20
    displayLine(dashes + sceneDescription + dashes)
  }
}
