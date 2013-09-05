package net.aicomp.terraforming

import java.applet.Applet

import scala.util.control.Exception.allCatch

class AppletReplayMain extends Applet {
  override def start() {
    val fps = allCatch opt getParameter("fps").toDouble getOrElse (15.0)
    val replay = getParameter("replay")
    Main.main(Array("-" + Main.LIGHT_GUI_MODE, "-" + Main.REPLAY_MODE, replay, "-" + Main.FPS, fps.toString));
  }
}