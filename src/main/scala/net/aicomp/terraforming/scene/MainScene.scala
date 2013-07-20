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
      displayLine("  build x y (robotmaker|excavator|attack|bridge|house|town)")
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
    val scores = game.players.map(player => (player.id, game.field.calculateScore(player)))
    val sortedScores = Sorting.stableSort(scores, (a:(Int,Int), b:(Int,Int)) => a._2 > b._2 || (a._2 == b._2 && a._1 > b._1))
    val ranking = Map.empty[Int,Int]
    for((sortedScores, rank) <- sortedScores.zipWithIndex) {
      ranking.updated(sortedScores._1, rank)
    }
    var stream: PrintStream = null
    try {
      val fileName = "result.txt"
      stream = new PrintStream(fileName)
      game.players.foreach { player => 
        stream.print(ranking.apply(player.id))
      }
    } catch {
      case _ => new PrintStream(new ByteArrayOutputStream())
    } finally {
      if(stream != null) {
        stream.close()
      }
    }
  }
}
