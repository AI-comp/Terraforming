package net.aicomp.terraforming.scene

import scala.collection.mutable.Queue

import net.aicomp.terraforming.entity.Game
import net.aicomp.terraforming.entity.GameEnvironment
import net.exkazuu.gameaiarena.gui.Scene
import net.exkazuu.gameaiarena.manipulator.ManipulatorResult
import net.exkazuu.gameaiarena.manipulator.ThreadManipulator

abstract class ManipultorScene(manipulators: Vector[ThreadManipulator[Game, Array[String], String]])
  extends AbstractScene {
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
          _result = Some(manipulators(game.currentPlayerIndex).run(game))
      }
    }
    var nextScene: Scene[GameEnvironment] = this;
    var lightMode = true;
    while (lightMode && !_commandStringQueue.isEmpty) {
      val commandString = _commandStringQueue.dequeue()
      displayLine("> " + commandString)
      nextScene = runWithCommandString(commandString)
      lightMode = ManipultorScene.lightMode
    }
    nextScene
  }

  protected def runWithCommandString(commandString: String): Scene[GameEnvironment]
}

object ManipultorScene {
  var lightMode = false
}