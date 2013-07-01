package net.aicomp.scene

import net.aicomp.entity.Command
import net.aicomp.entity.CommandException
import net.aicomp.entity.GameEnvironment
import net.exkazuu.gameaiarena.gui.Scene

abstract class MainScene(nextScene: Scene[GameEnvironment]) extends AbstractScene {
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
              game.acceptCommand(c(args))
              displayLine(game.field.toString)
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
