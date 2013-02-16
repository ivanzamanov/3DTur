package org.ivo.hilbert.turtle;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.vecmath.Color3f;

public class TurtleConfig {

	public static Appearance appearance = createDefaultAppearance();
	public static float segmentLength = 1f;
	public static double angle = Math.PI / 2;
	public static ITurtleDrawProxy drawProxy = new SmoothCurveDraw();

	private TurtleConfig() {
	}

	private static Appearance createDefaultAppearance() {
		final Appearance defaultAppearance = new Appearance();
		final Color3f specularColor = new Color3f(Color.black);
		final Color3f diffuseColor = new Color3f(Color.lightGray);
		final Color3f emissiveColor = new Color3f(Color.black);
		final Color3f ambientColor = new Color3f(Color.black);
		final Material pathMaterial = new Material(ambientColor, emissiveColor,
				diffuseColor, specularColor, 0);
		defaultAppearance.setMaterial(pathMaterial);
		return defaultAppearance;
	}
}
