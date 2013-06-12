package net.aicomp.scene

import org.specs2.mutable.SpecificationWithJUnit

import net.aicomp.entity.GameEnvironment

class PlayerSceneSpec extends SpecificationWithJUnit {
  val env = GameEnvironment()
  env.getSceneManager().setFps(1000)
  val mainScene = new MainScene(null) with TestScene
  val playerScene = new PlayerScene(mainScene) with TestScene
  def g = env.game

  "PlayerScene" should {
    "stores plyaer names ignoring wrong inputs" in {
      val nextScene = playerScene.acceptAll(env, "" :: "xxx" :: "abc def ghi" :: Nil)
      g.players(0).name must_== "abc"
      g.players(1).name must_== "def"
      g.players(2).name must_== "ghi"
      nextScene must_== mainScene
    }
  }
}