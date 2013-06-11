package net.aicomp.scene

import org.specs2.mutable.SpecificationWithJUnit
import net.aicomp.entity.GameEnvironment
import net.aicomp.entity.Installation

class MainSceneSpec extends SpecificationWithJUnit {
  val env = GameEnvironment()
  env.getSceneManager().setFps(1000)
  val mainScene = new MainScene(null) with TestScene
  val playerScene = new PlayerScene(mainScene) with TestScene
  def g = env.game

  "MainScene" should {
    "has a proper initial state" in {
      playerScene.accept(env, "abc def ghi")
      g.field.tiles.values.filter(_.ownedBy(g.players(0)))
        .map(_.installation.get).toList must_== List(Installation.initial)
      g.field.tiles.values.filter(_.ownedBy(g.players(1)))
        .map(_.installation.get).toList must_== List(Installation.initial)
      g.field.tiles.values.filter(_.ownedBy(g.players(2)))
        .map(_.installation.get).toList must_== List(Installation.initial)
    }
    "produce robots after finishing" in {
      playerScene.accept(env, "abc def ghi")
      mainScene.acceptAll(env, "finish" :: "finish" :: "finish" :: Nil)
      g.field.robotAmout(g.players(0)) must_== 10
      g.field.robotAmout(g.players(1)) must_== 5
      g.field.robotAmout(g.players(2)) must_== 5
    }
  }
}