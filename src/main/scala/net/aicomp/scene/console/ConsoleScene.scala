package net.aicomp.scene.console

import java.util.Scanner

import net.aicomp.scene.AbstractScene

trait ConsoleScene extends AbstractScene {
  override def displayCore(text: String) = print(text)
}
