package org.ivo.hilbert.utils;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;

public class Utils {

	public static void addAxes(final TransformGroup baseTransform) {
		addAxes(baseTransform, 1f, 0.005f);
	}

	public static void addAxes(final TransformGroup baseTransform,
			final float axisLength) {
		addAxes(baseTransform, axisLength, 0.005f);
	}

	public static void addAxes(final TransformGroup baseTransform,
			final float axisLength, final float axisWidth) {
		final Appearance xAppearance = new Appearance();
		xAppearance.setColoringAttributes(new ColoringAttributes());
		xAppearance.getColoringAttributes().setColor(new Color3f(Color.RED));

		final Appearance yAppearance = new Appearance();
		yAppearance.setColoringAttributes(new ColoringAttributes());
		yAppearance.getColoringAttributes().setColor(new Color3f(Color.GREEN));

		final Appearance zAppearance = new Appearance();
		zAppearance.setColoringAttributes(new ColoringAttributes());
		zAppearance.getColoringAttributes().setColor(new Color3f(Color.BLUE));

		final Transform3D xTransform = new Transform3D();
		xTransform.setTranslation(new Vector3f(axisLength, 0, 0));
		final Transform3D yTransform = new Transform3D();
		yTransform.setTranslation(new Vector3f(0, axisLength, 0));
		final Transform3D zTransform = new Transform3D();
		zTransform.setTranslation(new Vector3f(0, 0, axisLength));

		final Box xBox = new Box(axisLength, axisWidth, axisWidth, xAppearance);
		final Box yBox = new Box(axisWidth, axisLength, axisWidth, yAppearance);
		final Box zBox = new Box(axisWidth, axisWidth, axisLength, zAppearance);

		final TransformGroup xGroup = new TransformGroup(xTransform);
		xGroup.addChild(xBox);
		final TransformGroup yGroup = new TransformGroup(yTransform);
		yGroup.addChild(yBox);
		final TransformGroup zGroup = new TransformGroup(zTransform);
		zGroup.addChild(zBox);

		baseTransform.addChild(xGroup);
		baseTransform.addChild(yGroup);
		baseTransform.addChild(zGroup);
	}
}