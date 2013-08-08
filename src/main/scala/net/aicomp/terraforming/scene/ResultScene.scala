package net.aicomp.terraforming.scene

import net.exkazuu.gameaiarena.gui.Scene
import net.aicomp.terraforming.entity.GameSetting
import net.aicomp.terraforming.entity.GameEnvironment
import scala.util.Sorting
import java.io.PrintStream

class ResultScene(nextScene: Scene[GameEnvironment]) extends AbstractScene {
  final override def run() = {
    val s1 = "Player 1 (" + game.players(0).name + ") Score: " + game.field.calculateScore(game.players(0))
    val s2 = "Player 2 (" + game.players(1).name + ") Score: " + game.field.calculateScore(game.players(1))
    val s3 = "Player 3 (" + game.players(2).name + ") Score: " + game.field.calculateScore(game.players(2))
    displayLine(s1)
    displayLine(s2)
    displayLine(s3)
    println("------------------ Result Summary ------------------")
    println(calculateRank())
    println(s1)
    println(s2)
    println(s3)
    nextScene
  }

  def calculateRank() = {
    val scores = game.players.map(player => (player.id, game.field.calculateScore(player)))
    val sortedScores = Sorting.stableSort(scores,
      (a: (Int, Int), b: (Int, Int)) => a._2 > b._2 || (a._2 == b._2 && a._1 > b._1))
    val id2Rank = sortedScores.zipWithIndex.map { case ((id, score), rank) => (id, rank + 1) }.toMap
    var stream: PrintStream = null
    try {
      stream = new PrintStream("result.txt")
      val ret = game.players.map(p => id2Rank(p.id)).mkString(" ")
      stream.println(ret)
      ret
    } catch {
      case _: Throwable => ""
    } finally {
      if (stream != null) {
        stream.close()
      }
    }
  }
}
