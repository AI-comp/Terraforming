package net.aicomp.scene

import net.aicomp.scene.console.ConsoleScene
import scala.collection.mutable.Queue
import net.exkazuu.gameaiarena.gui.Scene
import net.aicomp.entity.GameEnvironment
import org.specs2.specification.Scope
import net.aicomp.entity.Game
import net.aicomp.entity.Field
import net.aicomp.entity.Player

trait TestScene extends ConsoleScene {
  val output = Queue[String]()
  private val input = Queue[String]()
  private var lastScene: Scene[GameEnvironment] = this

  override def run() = {
    lastScene = runWithCommand(input.dequeue().split(" ").toList)
    null
  }
  override def nextCommandStrnigs = throw new Exception()
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

trait TestSceneInitializer extends Scope {
  val env = GameEnvironment()
  val players = Range(0, 3).map(new Player(_)).toList
  val field = Field(7, players)
  env.game = new Game(field, players, 2 * 3)
  env.getSceneManager().setFps(1000)
  val mainScene = new MainScene(null) with TestScene
  val playerScene = new PlayerScene(mainScene) with TestScene
  def g = env.game
}
