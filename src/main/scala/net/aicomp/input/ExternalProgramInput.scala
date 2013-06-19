package net.aicomp.input

import scala.collection.mutable.ListBuffer

import net.aicomp.entity.Game
import net.exkazuu.gameaiarena.player.ExternalComputerPlayer

class ExternalProgramInput(commandAndArgs: Array[String]) extends ExternalComputerPlayer(commandAndArgs) with Input {
  def this(commandString: String) = this(commandString.split(" "))

  def inputCommandLists(game: Game) = {
    if (game != null) {
      val s = game.stringify
      super.writeLine(s)
    }
    val buf = ListBuffer[String]()
    // TODO: Rewrite the following code to use more functional features
    var line = ""
    do {
      line = super.readLine
      buf += line
    } while (line.trim != "")
    buf.map(_.split(" ").toArray).toArray
  }
}
