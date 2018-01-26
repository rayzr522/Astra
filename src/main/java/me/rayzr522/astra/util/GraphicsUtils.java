package me.rayzr522.astra.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class GraphicsUtils {

	public static void setAntialiasing(Graphics2D g, boolean on) {

		if (on) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		else if (!on) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

	}

}
