package net.aicomp.scene

import net.aicomp.scene.console.ConsoleScene
import scala.collection.mutable.Queue
import jp.ac.waseda.cs.washi.gameaiarena.gui.Scene
import net.aicomp.entity.GameEnvironment

trait TestScene extends ConsoleScene {
  val output = Queue[String]()
  private val input = Queue[String]()
  private var lastScene: Scene[GameEnvironment] = this

  override def run() = {
    lastScene = runWithArgs(input.dequeue().split(" ").filter(_.length > 0).toList)
    null
  }
  override def nextCommand = throw new Exception()
  override def displayCore(text: String) { output += text }

  def accept(env: GameEnvironment, command: String) = {
    input += command
    env.start(this)
    lastScene
  }

  def acceptAll(env: GameEnvironment, commands: Seq[String]) = {
    commands.foreach { command =>
      input += command
      env.start(this)
    }
    lastScene
  }
}
