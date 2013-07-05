package net.aicomp.terraforming.scene

import scala.collection.mutable.Queue

import org.specs2.specification.Scope

import net.aicomp.terraforming.entity.Field
import net.aicomp.terraforming.entity.Game
import net.aicomp.terraforming.entity.GameEnvironment
import net.aicomp.terraforming.entity.Player
import net.aicomp.terraforming.scene.console.ConsoleScene

trait TestScene extends ManipultorScene with ConsoleScene {
  val output = Queue[String]()
  private val input = Queue[String]()

  override def run() = {
    runWithCommandString(input.dequeue())
  }
  override def displayCore(text: String) { output += text }

  def accept(env: GameEnvironment, command: String) = {
    input += command
    env.getSceneManager().runOneStep(env, this)
  }

  def acceptAll(env: GameEnvironment, commands: Iterable[String]) = {
    commands.map { command =>
      input += command
      env.getSceneManager().runOneStep(env, this)
    }.last
  }
}

trait TestSceneInitializer extends Scope {
  val env = GameEnvironment()
  val players = Vector(new Player(0), new Player(1), new Player(2))
  val field = Field(7, players)
  env.game = new Game(field, players, 2 * 3)
  env.getSceneManager().setFps(1000)
  val mainScene = new MainScene(null) with TestScene
  val playerScene = new PlayerScene(mainScene) with TestScene
  def g = env.game
  env.getSceneManager().initialize(env, playerScene)
}