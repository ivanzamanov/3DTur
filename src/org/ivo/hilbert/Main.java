package org.ivo.hilbert;

import java.applet.Applet;

import javax.swing.WindowConstants;

import com.sun.j3d.utils.applet.JMainFrame;

public class Main {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Applet applet = new Hilbert3D();
		final JMainFrame frame = new JMainFrame(applet, 900, 900);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
