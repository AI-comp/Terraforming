package net.aicomp.scene

import jp.ac.waseda.cs.washi.gameaiarena.gui.Scene
import net.aicomp.entity.GameEnvironment

abstract class MainScene(val nextScene: Scene[GameEnvironment]) extends AbstractScene {
  def runWithArgs(commandAndArgs: List[String]) = {
    require(commandAndArgs != Nil)

    val commands = Map(
      "move" -> game.moveCommand _,
      "build" -> game.buildCommand _
    )
    def help = {
      println("Commands:")
      println("  move x y (r|ur|dr|l|ul|dl)")
      println("  build x y (br|sh|at|mt|pk|sq|pl)")
    }
    
    val cmd :: args = commandAndArgs
    commands.get(cmd) match {
      case Some(c) => c(args)
      case None    => help
    }

    println(game.field)
    this
  }
}
