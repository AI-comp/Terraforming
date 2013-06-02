package net.aicomp.scene

import jp.ac.waseda.cs.washi.gameaiarena.gui.Scene
import net.aicomp.entity.Game
import net.aicomp.entity.GameEnvironment
import java.io.File
import com.google.common.io.Files
import net.aicomp.util.settings.Defaults
import net.aicomp.util.misc.DateUtils
import java.nio.charset.Charset
import net.aicomp.util.misc.Logger

abstract class AbstractScene extends Scene[GameEnvironment] {
  def env = getEnvironment
  def renderer = env.getRenderer
  def inputer = env.getInputer
  def game = env.game

  override def run() = {
    nextCommand match {
      case Some(command) =>
        displayLine("> " + command.mkString(" "))
        if (command.length > 0) runWithArgs(command) else AbstractScene.this
      case None =>
        AbstractScene.this
    }
  }

  protected def nextCommand: Option[List[String]]

  protected def runWithArgs(words: List[String]): Scene[GameEnvironment]

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
