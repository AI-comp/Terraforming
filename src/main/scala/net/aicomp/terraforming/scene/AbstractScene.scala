package net.aicomp.terraforming.scene

import net.aicomp.terraforming.entity.GameEnvironment
import net.aicomp.terraforming.util.misc.Logger
import net.aicomp.terraforming.util.settings.Defaults
import net.exkazuu.gameaiarena.gui.DefaultScene

abstract class AbstractScene extends DefaultScene[GameEnvironment] {
  def env = getEnvironment
  def renderer = env.getRenderer
  def inputer = env.getInputer
  def game = env.game

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
