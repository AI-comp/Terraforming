package net.aicomp.terraforming.scene

import net.aicomp.terraforming.entity.Command
import net.aicomp.terraforming.entity.CommandException
import net.aicomp.terraforming.entity.Game
import net.aicomp.terraforming.entity.GameEnvironment
import net.aicomp.terraforming.entity.GameSetting
import net.exkazuu.gameaiarena.gui.Scene
import net.exkazuu.gameaiarena.manipulator.ThreadManipulator
import java.io.PrintStream
import scala.util.Sorting
import java.io.ByteArrayOutputStream

class MainScene(nextScene: Scene[GameEnvironment],
  manipulators: Vector[ThreadManipulator[Game, Array[String], String]] = Vector(),
  jsonStream: PrintStream = null)
  extends ManipultorScene(manipulators) {

  override def initialize() {
    displayLine(game.startTurn())
    writeJson()
  }

  override def runWithCommandString(commandString: String) = {
    require(commandString != null)

    val commands = Map(
      "move" -> Command.moveCommand,
      "build" -> Command.buildCommand,
      "reset" -> Command.resetCommand,
      "finish" -> Command.finishCommand)
    def help = {
      displayLine("Commands:")
      displayLine("  move x y (r|ur|dr|l|ul|dl) robot_amount")
      displayLine("      Move robots on the (x, y) tile with the specified direction")
      displayLine("  build x y (robotmaker|excavator|tower|bridge|house|town)")
      displayLine("      Build the specified installation on the (x, y) tile")
      displayLine("  reset")
      displayLine("      Reset the initial state of the current turn.")
      displayLine("  finish")
      displayLine("      Finish the current turn and change to the next turn.")
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
              game.acceptCommand(c(args)) match {
                case ret: String => {
                  displayLine(ret.toString())
                }
                case game: Game => {
                  env.game = game
                }
                case _ => ()
              }
              writeJson()
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

  def writeJson() = if (jsonStream != null) jsonStream.println(game.toJson(game.currentPlayer))
  
  override def release() {
    
  }
}
