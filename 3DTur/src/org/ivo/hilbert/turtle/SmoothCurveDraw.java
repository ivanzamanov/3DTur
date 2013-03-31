package org.ivo.hilbert.turtle;

import java.util.Iterator;
import java.util.List;

import javax.media.j3d.Group;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Sphere;

public class SmoothCurveDraw implements ITurtleDrawProxy {

	private final int iTERATIONS = 40;
	private final float STEP = 1f / iTERATIONS;

	private final float RADIUS = 0.02f;

	// private final float LENGTH = 0.02f;

	@Override
	public void draw(final List<Vector3f> vertices, final Group target,
			final List<Transform3D> rotations) {
		final Iterator<Vector3f> verticesIterator = vertices.iterator();
		Vector3f pPrev = verticesIterator.next();
		Vector3f prev = verticesIterator.next();
		Vector3f next = verticesIterator.next();

		Vector3f mid1 = getMid(pPrev, prev);
		Vector3f mid2 = getMid(prev, next);
		drawCurve(mid1, prev, mid2, target);

		while (verticesIterator.hasNext()) {
			pPrev = prev;
			prev = next;
			next = verticesIterator.next();

			mid1 = getMid(pPrev, prev);
			mid2 = getMid(prev, next);
			drawCurve(mid1, prev, mid2, target);
		}
	}

	@Override
	public void draw(final List<Vector3f> vertices, final Group target) {
		throw new UnsupportedOperationException("Not implemented.");
	}

	private Vector3f getMid(final Vector3f p1, final Vector3f p2) {
		final Vector3f result = new Vector3f(p1);
		result.add(p2);
		result.scale(0.5f);
		return result;
	}

	private void drawCurve(final Vector3f from, final Vector3f control,
			final Vector3f to, final Group target) {

		// Reuse transform and vectors.
		final Transform3D transform = new Transform3D();
		final Vector3f tmp = new Vector3f();
		final Vector3f pPrevSave = new Vector3f();
		final Vector3f prevSave = new Vector3f();
		final Vector3f nextSave = new Vector3f();

		for (float i = 1; i <= iTERATIONS; i++) {
			final Group body = createBody();

			final float t = i * STEP;
			pPrevSave.set(from);
			prevSave.set(control);
			nextSave.set(to);

			// Simple quadratic Bezier "curve".
			final float t1 = 1f - t;
			pPrevSave.scale(t1 * t1);
			prevSave.scale(2 * t1 * t);
			nextSave.scale(t * t);

			tmp.set(0, 0, 0);

			tmp.add(pPrevSave);
			tmp.add(prevSave);
			tmp.add(nextSave);

			transform.setIdentity();
			// Rotate the body based on how far in the curve we are.
			addCurveRotation(transform, i, iTERATIONS, from, control, to);
			// Translation to the current point of the curve.
			transform.setTranslation(tmp);
			final TransformGroup translationTransformGroup = new TransformGroup(
					transform);

			transform.setIdentity();
			final Vector3f startAdjustingTranslation = new Vector3f();
			transform.setTranslation(startAdjustingTranslation);
			final TransformGroup adjustingTransformGroup = new TransformGroup(
					transform);

			adjustingTransformGroup.addChild(body);
			translationTransformGroup.addChild(adjustingTransformGroup);
			target.addChild(translationTransformGroup);
		}
	}

	private Group createBody() {
		return new Sphere(RADIUS, TurtleConfig.appearance);
	}

	/*
	 * Ideally, this should rotate the body (originally - a cylinder), so it
	 * will look like a part of a continuous pipe, but...
	 * "Ain't nobody got time for that."
	 */
	private void addCurveRotation(final Transform3D transform, final float i,
			final int iterations, final Vector3f from, final Vector3f control,
			final Vector3f to) {
		final Vector3f tmp1 = new Vector3f(control);
		final Vector3f tmp2 = new Vector3f(control);
		tmp1.add(from);
		tmp2.add(to);

		final Vector3f axis = new Vector3f();
		axis.cross(tmp1, tmp2);
		final AxisAngle4f axisAngle = new AxisAngle4f(axis, (float) ((1 - i
				/ iterations)
				* -Math.PI / 2));
		transform.setRotation(axisAngle);
	}
}
