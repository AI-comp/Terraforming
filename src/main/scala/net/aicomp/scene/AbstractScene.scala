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

  private val _input = Queue[List[String]]()
  private val _thread = new Thread {
    private var _commandStrings: Option[Array[String]] = None
    private var _enabled = false

    def nextCommandStrings = {
      _enabled = true
      if (!this.isAlive()) {
        this.start
      }
      synchronized {
        _commandStrings match {
          case Some(commandStrings) => {
            _commandStrings = None
            commandStrings
          }
          case None => Array("")
        }
      }
    }

    override def run() {
      while (true) {
        Thread.sleep(10)
        if (_enabled) {
          val commandStrings = runManipulator
          synchronized {
            _commandStrings = Some(commandStrings)
            _enabled = false
          }
        }
      }
    }
  }

  override def run() = {
    if (_input.isEmpty) {
      val commandStrings = _thread.nextCommandStrings
        .filter(_ != null)
        .map(_.split(" "))
        .map(_.filter(_.length > 0).toList)
        .filter(_.length > 0)
      for (commandString <- commandStrings) {
        _input.enqueue(commandString)
      }
    }
    if (!_input.isEmpty) {
      val commandString = _input.dequeue()
      displayLine("> " + commandString.mkString(" "))
      runWithCommand(commandString)
    } else {
      AbstractScene.this
    }
  }

  protected def runManipulator = game.currentPlayer.gameManipulator.run(game)

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
