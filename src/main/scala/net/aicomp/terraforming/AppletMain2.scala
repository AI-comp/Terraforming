package net.aicomp.terraforming

import java.applet.Applet
import scala.util.control.Exception.allCatch
import java.awt.Toolkit
import java.net.URL

class AppletMain2 extends Applet {
  override def start() {
    val fps = allCatch opt getParameter("fps").toDouble getOrElse (15.0)
    //Main.main(Array("-" + Main.FPS, fps.toString));
    
    val cl = this.getClass().getClassLoader()
    println("getCodeBase: " + getCodeBase())
    println("getDocumentBase: " + getDocumentBase())
    println(getImage(cl.getResource("/img/hex/hex32.png")))
    println(getImage(getCodeBase(), "/img/hex/hex32.png"))

    val url = cl.getResource("/img/hex/hex32.png")
    println(url)
    val img = Toolkit.getDefaultToolkit().getImage(url)
    println(img)

    try {
      Main.main(Array("-u", "3"));
    } catch {
      case e =>
        e.printStackTrace(System.err);
    }
  }
}