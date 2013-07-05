package net.aicomp.terraforming.scene.console

import java.util.Scanner
import net.aicomp.terraforming.scene.AbstractScene

trait ConsoleScene extends AbstractScene {
  override def displayCore(text: String) = print(text)
}
