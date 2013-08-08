package net.aicomp.terraforming.scene

import scala.collection.mutable.Queue
import org.specs2.specification.Scope
import net.aicomp.terraforming.entity.Field
import net.aicomp.terraforming.entity.Game
import net.aicomp.terraforming.entity.GameEnvironment
import net.aicomp.terraforming.entity.Player
import java.util.Random
import net.aicomp.terraforming.entity.Installation

trait TestScene extends ManipultorScene {
  val output = Queue[String]()
  private val input = Queue[String]()
  AbstractScene.display = { (text: String) => output += text }

  override def run() = {
    runWithCommandString(input.dequeue())
  }

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
  val players = Vector(Player(0), Player(1), Player(2))
  val field = Field(7, players, new Random(0))
  env.game = new Game(field, players, 2)
  env.getSceneManager().setFps(1000)
  val resultScene = new ResultScene(null)
  val mainScene = new MainScene(resultScene) with TestScene
  val playerScene = new PlayerScene(mainScene) with TestScene
  def g = env.game
  env.getSceneManager().initialize(env, playerScene)

  def initialPoints(id: Int) = {
    field.tiles.keys.find(p => {
      field.tiles(p).installation.exists(_ == Installation.initial) && field.tiles(p).owner.get.id == id
    }).get
  }
}
