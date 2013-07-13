package net.aicomp.terraforming;

import java.applet.Applet;

import com.google.common.base.Strings;

public class AppletMain extends Applet {
	private static final long serialVersionUID = 2341942848982018935L;

	@Override
	public void start() {
		String fpsString = getParameter("fps");
		if (Strings.isNullOrEmpty(fpsString)) {
			fpsString = "30";
		}
		// Main.main(new String[] { "-" + Main.FPS(), fpsString });
		Main.main(new String[] { "-u", "3" });
	}
}