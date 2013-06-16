package net.aicomp.scene

import net.aicomp.entity.GameEnvironment
import net.aicomp.util.misc.Logger
import net.aicomp.util.settings.Defaults
import net.exkazuu.gameaiarena.gui.DefaultScene
import net.exkazuu.gameaiarena.gui.Scene
import scala.collection.mutable.Queue

abstract class AbstractScene extends DefaultScene[GameEnvironment] {
  def env = getEnvironment
  def renderer = env.getRenderer
  def inputer = env.getInputer
  def game = env.game
  private val input = Queue[List[String]]()

  override def run() = {
    if (input.isEmpty) {
      val commandStrings = nextCommandStrings
        .filter(_ != null)
        .map(_.filter(_.length > 0).toList)
        .filter(_.length > 0)
      for (commandString <- commandStrings) {
        input.enqueue(commandString)
      }
    }
    if (!input.isEmpty) {
      val commandString = input.dequeue()
      displayLine("> " + commandString.mkString(" "))
      runWithCommand(commandString)
    } else {
      AbstractScene.this
    }
  }

  protected def nextCommandStrings = game.currentPlayer.manipulator.run(game)

  protected def runWithCommand(commandString: List[String]): Scene[GameEnvironment]

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
