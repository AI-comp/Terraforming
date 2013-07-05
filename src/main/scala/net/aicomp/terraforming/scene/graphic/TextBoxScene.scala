package net.aicomp.terraforming.scene.graphic

import scala.collection.mutable.Queue
import net.aicomp.terraforming.scene.AbstractScene

trait TextBoxScene extends AbstractScene {
  override def displayCore(text: String) = TextBoxScene.display(text)
}

object TextBoxScene {
  private var commands = Queue[String]()

  var display: (String => Unit) = null

  def addCommand(command: String) = commands.enqueue(command)

  def inputCommandLists() = {
    if (commands.size > 0)
      Some(commands.dequeue)
    else
      None
  }
}
