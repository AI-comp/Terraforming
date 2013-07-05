package net.aicomp.scene

import org.specs2.mutable.SpecificationWithJUnit

import net.aicomp.entity.GameEnvironment
import net.aicomp.entity.Installation
import org.specs2.specification.Scope

class MainSceneSpec extends SpecificationWithJUnit {
  "MainScene" should {
    "has a proper initial state" in new TestSceneInitializer {
      playerScene.acceptAll(env, "abc" :: "def" :: "ghi" :: Nil)
      g.field.tiles.values.filter(_.ownedBy(g.players(0)))
        .map(_.installation.get).toList must_== List(Installation.initial)
      g.field.tiles.values.filter(_.ownedBy(g.players(1)))
        .map(_.installation.get).toList must_== List(Installation.initial)
      g.field.tiles.values.filter(_.ownedBy(g.players(2)))
        .map(_.installation.get).toList must_== List(Installation.initial)
    }
    "produce robots after finishing" in new TestSceneInitializer {
      g.field.robotAmount(g.players(0)) must_== 0
      g.field.robotAmount(g.players(1)) must_== 0
      g.field.robotAmount(g.players(2)) must_== 0
      playerScene.acceptAll(env, "abc" :: "def" :: "ghi" :: Nil)
      g.field.robotAmount(g.players(0)) must_== 5
      g.field.robotAmount(g.players(1)) must_== 0
      g.field.robotAmount(g.players(2)) must_== 0
      mainScene.acceptAll(env, "finish" :: "finish" :: "finish" :: Nil)
      g.field.robotAmount(g.players(0)) must_== 10
      g.field.robotAmount(g.players(1)) must_== 5
      g.field.robotAmount(g.players(2)) must_== 5
    }
  }
}
