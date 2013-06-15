package net.aicomp.scene.graphic

import scala.collection.mutable.Queue

import net.aicomp.scene.AbstractScene

trait TextBoxScene extends AbstractScene {
  override def displayCore(text: String) = TextBoxScene.display(text)
}

object TextBoxScene {
  private var commands = Queue[String]()

  var display: (String => Unit) = null

  def addCommand(command: String) = commands.enqueue(command)

  def inputCommandLists() = {
    if (commands.size > 0)
      Array(commands.dequeue.split(" ").toArray)
    else
      Array(Array[String]())
  }
}
