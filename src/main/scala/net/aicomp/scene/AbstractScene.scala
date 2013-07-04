package net.aicomp.scene

import scala.collection.mutable.Queue

import net.aicomp.entity.GameEnvironment
import net.aicomp.util.misc.Logger
import net.aicomp.util.settings.Defaults
import net.exkazuu.gameaiarena.gui.DefaultScene
import net.exkazuu.gameaiarena.gui.Scene
import net.exkazuu.gameaiarena.manipulator.ManipulatorResult

abstract class AbstractScene extends DefaultScene[GameEnvironment] {
  def env = getEnvironment
  def renderer = env.getRenderer
  def inputer = env.getInputer
  def game = env.game

  private val _commandStringQueue = Queue[String]()
  private var _result: Option[ManipulatorResult[Array[String]]] = None

  override def run() = {
    if (_commandStringQueue.isEmpty) {
      _result match {
        case Some(result) =>
          if (result.isFinished()) {
            for (commandString <- result.getResult().filter(_ != null)) {
              _commandStringQueue.enqueue(commandString)
            }
            _result = None
          }
        case None =>
          _result = Some(runManipulator)
      }
    }
    if (!_commandStringQueue.isEmpty) {
      val commandString = _commandStringQueue.dequeue()
      displayLine("> " + commandString)
      runWithCommandString(commandString)
    } else {
      AbstractScene.this
    }
  }

  protected def runManipulator = game.currentPlayer.gameManipulator.run(game)

  protected def runWithCommandString(commandString: String): Scene[GameEnvironment]

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
