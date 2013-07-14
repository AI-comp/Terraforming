package net.aicomp.terraforming;

import java.applet.Applet;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import com.google.common.base.Strings;

public class AppletMain extends Applet {
  private static final long serialVersionUID = 2341942848982018935L;

  @Override
  public void init() {
    System.out.println("before init");
    ClassLoader cl = this.getClass().getClassLoader();
    System.out.println("getCodeBase: " + getCodeBase());
    System.out.println("getDocumentBase: " + getDocumentBase());
    System.out.println(getImage(getCodeBase(), "img/hex/hex32.png"));

    URL url = cl.getResource("img/hex/hex32.png");
    System.out.println(url);
    Image img = Toolkit.getDefaultToolkit().getImage(url);
    System.out.println(img);
    System.out.println("after init");
  }
  
  @Override
  public void start() {
    String fpsString = getParameter("fps");

    ClassLoader cl = this.getClass().getClassLoader();
    System.out.println("getCodeBase: " + getCodeBase());
    System.out.println("getDocumentBase: " + getDocumentBase());
    System.out.println(getImage(getCodeBase(), "/img/hex/hex32.png"));

    URL url = cl.getResource("img/hex/hex32.png");
    System.out.println(url);
    Image img = Toolkit.getDefaultToolkit().getImage(url);
    System.out.println(img);

    if (Strings.isNullOrEmpty(fpsString)) {
      fpsString = "30";
    }
    // Main.main(new String[] { "-" + Main.FPS(), fpsString });
    try {
      Main.main(new String[] {"-u", "3"});
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }
}
