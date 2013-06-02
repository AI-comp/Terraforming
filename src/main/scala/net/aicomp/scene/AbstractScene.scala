package net.aicomp.scene

import java.io.File
import java.nio.charset.Charset

import com.google.common.io.Files

import jp.ac.waseda.cs.washi.gameaiarena.gui.Scene
import net.aicomp.entity.Game
import net.aicomp.entity.GameEnvironment
import net.aicomp.util.misc.DateUtils
import net.aicomp.util.settings.Defaults

abstract class AbstractScene extends Scene[GameEnvironment] {

  new File("log").mkdir()
  val writer = Files.newWriter(new File("log/log_" + DateUtils.dateStringForFileName + ".txt"), Charset.defaultCharset)

  def env = getEnvironment
  def renderer = env.getRenderer
  def inputer = env.getInputer

  def game = env.game
  def game_=(game: Game) = {
    getEnvironment.game = game
    game
  }

  def execute(): (Game, Scene[GameEnvironment])

  final override def run() = {
    val (newGame, scene) = execute()
    game = newGame
    scene
  }

  protected def displayCore(text: String)

  final def display(text: String) {
    displayCore(text)
    writer.write(text)
    writer.flush()
  }

  final def displayLine(text: String) {
    display(text + Defaults.NEW_LINE)
  }

  final def describe(sceneDescription: String) {
    val dashes = "-" * 20
    displayLine(dashes + sceneDescription + dashes)
  }
}
