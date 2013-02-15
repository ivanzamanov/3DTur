package org.ivo.hilbert.turtle.path;

import javax.media.j3d.Appearance;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import org.ivo.hilbert.turtle.BodySegment;
import org.ivo.hilbert.turtle.TurtleConfig;

import com.sun.j3d.utils.geometry.Box;

public class BoxSegment extends BodySegment {

	private static final float BOX_LENGTH = 0.2f;

	@Override
	public TransformGroup interpret(final TransformGroup target, final TurtleConfig config) {

		final Appearance appearance = config.getAppearance();
		final Box box = new Box(0.2f, 0.1f, 0.03f, appearance);

		final Transform3D resultTransform = new Transform3D();
		resultTransform.setTranslation(new Vector3f(BOX_LENGTH, 0, 0));
		final TransformGroup result = new TransformGroup(resultTransform);
		result.addChild(box);

		final Transform3D postTransform = new Transform3D();
		postTransform.setTranslation(new Vector3f(BOX_LENGTH * 2, 0, 0));

		final TransformGroup newGroup = new TransformGroup(postTransform);
		result.addChild(newGroup);

		return result;
	}

}
