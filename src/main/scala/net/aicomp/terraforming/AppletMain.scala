package net.aicomp.terraforming

import java.applet.Applet

import scala.util.control.Exception.allCatch

class AppletMain extends Applet {
  override def start() {
    val fps = allCatch opt getParameter("fps").toDouble getOrElse (15.0)
    val user = allCatch opt getParameter("user").toInt getOrElse (1)
    Main.main(Array("-l", "-" + Main.USER_PLAYERS, user.toString, "-" + Main.FPS, fps.toString));
  }
}