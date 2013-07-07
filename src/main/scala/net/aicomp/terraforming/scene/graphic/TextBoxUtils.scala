package net.aicomp.terraforming.scene.graphic

import scala.collection.mutable.Queue
import net.aicomp.terraforming.scene.AbstractScene

object TextBoxUtils {
  private var commands = Queue[String]()

  def addCommand(command: String) = commands.enqueue(command)

  def inputCommandLists() = {
    if (commands.size > 0)
      Some(commands.dequeue)
    else
      None
  }
}
