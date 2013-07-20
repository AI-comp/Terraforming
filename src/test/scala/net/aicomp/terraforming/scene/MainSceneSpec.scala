package net.aicomp.terraforming.scene

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope
import net.aicomp.terraforming.entity.Installation
import scala.io.Source
import java.io.File

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
    "reset a current turn" in new TestSceneInitializer {
      g.field.robotAmount(g.players(0)) must_== 0
      g.field.robotAmount(g.players(1)) must_== 0
      g.field.robotAmount(g.players(2)) must_== 0
      playerScene.acceptAll(env, "abc" :: "def" :: "ghi" :: Nil)
      g.field.robotAmount(g.players(0)) must_== 5
      g.field.robotAmount(g.players(1)) must_== 0
      g.field.robotAmount(g.players(2)) must_== 0
      mainScene.accept(env, "move " + initialPoints(0).x + " " + initialPoints(0).y + " l 4")
      g.field(initialPoints(0)).robots must_== 1
      mainScene.accept(env, "reset")
      g.field(initialPoints(0)).robots must_== 5
    }
    "generate a result text (1 2 3)" in new TestSceneInitializer {
      playerScene.acceptAll(env, "abc" :: "def" :: "ghi" :: Nil)
      mainScene.accept(env, "move " + initialPoints(2).x + " " + initialPoints(2).y + " l 1")
      mainScene.accept(env, "finish")
      mainScene.accept(env, "move " + initialPoints(1).x + " " + initialPoints(1).y + " l 1")
      mainScene.accept(env, "finish")
      mainScene.accept(env, "finish")
      mainScene.accept(env, "move " + initialPoints(2).x + " " + initialPoints(2).y + " r 1")
      mainScene.acceptAll(env, "finish" :: "finish" :: "finish" :: Nil)
      val fileName = "result.txt"
      val file = new File(fileName);
      val line = Source.fromFile(file).getLines().toSeq.head
      line.toString must_== "3 2 1"
    }
  }
}
