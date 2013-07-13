package net.aicomp.terraforming

import java.applet.Applet

import scala.util.control.Exception.allCatch

class AppletMain2 extends Applet {
  override def start() {
    val fps = allCatch opt getParameter("fps").toDouble getOrElse (15.0)
    //Main.main(Array("-" + Main.FPS, fps.toString));
    Main.main(Array("-u", "3"));
  }
}