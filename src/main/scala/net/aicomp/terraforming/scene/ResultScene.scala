package net.aicomp.terraforming.scene

import net.exkazuu.gameaiarena.gui.Scene
import net.aicomp.terraforming.entity.GameSetting
import net.aicomp.terraforming.entity.GameEnvironment
import scala.util.Sorting
import java.io.PrintStream

class ResultScene(nextScene: Scene[GameEnvironment], stream: PrintStream = null) extends AbstractScene {
  var rankString = ""

  final override def run() = {
    val s1 = "Player 1 (" + game.players(0).name + ") Score: " + game.field.calculateScore(game.players(0))
    val s2 = "Player 2 (" + game.players(1).name + ") Score: " + game.field.calculateScore(game.players(1))
    val s3 = "Player 3 (" + game.players(2).name + ") Score: " + game.field.calculateScore(game.players(2))
    rankString = calculateRank()

    displayLine(s1)
    displayLine(s2)
    displayLine(s3)
    println("------------------ Result Summary ------------------")
    println(rankString)
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
    val ranks = game.players.map(p => id2Rank(p.id)).mkString(",")
    if (stream != null) {
      val names = game.players.map(p => '"' + p.name + '"').mkString(",")
      val scores = game.players.map(p => game.field.calculateScore(p)).mkString(",")
      stream.print("\"names\":[" + names + "],")
      stream.print("\"scores\":[" + scores + "],")
      stream.print("\"ranks\":[" + ranks + "]},")
      stream.println()
      stream.close()
    }
    ranks
  }
}
