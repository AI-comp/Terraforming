package net.aicomp.terraforming.scene

import net.aicomp.terraforming.entity.Command
import net.aicomp.terraforming.entity.CommandException
import net.aicomp.terraforming.entity.Game
import net.aicomp.terraforming.entity.GameEnvironment
import net.aicomp.terraforming.entity.GameSetting
import net.exkazuu.gameaiarena.gui.Scene
import net.exkazuu.gameaiarena.manipulator.ThreadManipulator

abstract class MainScene(nextScene: Scene[GameEnvironment],
  manipulators: Vector[ThreadManipulator[Game, Array[String], String]] = Vector())
  extends ManipultorScene(manipulators) {
  override def initialize() {
    displayLine(game.startTurn())
  }

  override def runWithCommandString(commandString: String) = {
    require(commandString != null)

    val commands = Map(
      "move" -> Command.moveCommand,
      "build" -> Command.buildCommand,
      "finish" -> Command.finishCommand)
    def help = {
      displayLine("Commands:")
      displayLine("  move x y (r|ur|dr|l|ul|dl) robot_amount")
      displayLine("  build x y (br|sh|at|mt|pk|sq|pl)")
      displayLine("  finish")
    }

    val commandWithArgs = commandString
      .split(" ")
      .filter(_.length > 0)
      .toList

    commandWithArgs match {
      case cmd :: args => {
        commands.get(cmd) match {
          case Some(c) => {
            try {
              val ret = game.acceptCommand(c(args))
              if (ret.isInstanceOf[String]) {
                displayLine(ret.toString())
              }
            } catch {
              case CommandException(msg) => {
                displayLine(msg)
                help
              }
            }
          }
          case None => help
        }
      }
      case _ => help
    }

    if (!game.isFinished)
      this
    else
      nextScene
  }
}
