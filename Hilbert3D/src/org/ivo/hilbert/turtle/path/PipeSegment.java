package org.ivo.hilbert.turtle.path;

import java.util.List;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.ivo.hilbert.turtle.BodySegment;
import org.ivo.hilbert.turtle.PathSegment;
import org.ivo.hilbert.turtle.TurtleConfig;

import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;

public class PipeSegment extends BodySegment {

	private static final int ITERATIONS = 4;
	private static final int VERTICES_PER_ITERATION = 4;
	private static final int NUM_VERTICES = VERTICES_PER_ITERATION * ITERATIONS;
	private static final float RADIUS = 0.1f;

	@Override
	public TransformGroup interpret(final TransformGroup target,
			final List<PathSegment> previousSegments, final TurtleConfig config) {

		// final GeometryArray quads = new QuadArray(NUM_VERTICES,
		// GeometryArray.COORDINATES);
		// quads.setCoordinates(0, coordinates);
		// final GeometryInfo info = new GeometryInfo(quads);
		// final Geometry geom = info.getGeometryArray();
		// final Shape3D shape = new Shape3D(geom, appearance);

		final Transform3D transform = new Transform3D();
		final Vector3f vector = new Vector3f();

		// The total length of the generated body.
		final float totalLength = config.getSegmentLength();

		final Cylinder cyl = new Cylinder(RADIUS, totalLength,
				config.getAppearance());

		// Rotate the cylinder so it's Z axis will align with our X axis.
		transform.setIdentity();
		transform.rotX(Math.PI / 2);
		transform.rotY(Math.PI / 2);
		transform.rotZ(Math.PI / 2);
		// Translate so the cylinder's "bottom" edge will align to (RADIUS,0,0)
		vector.set(totalLength / 2, 0, 0);
		transform.setTranslation(vector);

		final TransformGroup cylinderGroup = new TransformGroup(transform);
		cylinderGroup.addChild(cyl.getShape(Cylinder.BODY).cloneTree());

		target.addChild(cylinderGroup);
		// Just add the sphere at (0,0,0) so adjecent segments will overlap.
		target.addChild(new Sphere(RADIUS, config.getAppearance()));

		// Next segments will be
		// appended to the "end" of the body.
		vector.set(config.getSegmentLength(), 0, 0);
		transform.setIdentity();
		transform.setTranslation(vector);
		final TransformGroup resultGroup = new TransformGroup(transform);
		target.addChild(resultGroup);

		return resultGroup;
	}

	@SuppressWarnings("unused")
	private static Point3f[] generateCoordinates(final float length) {
		final Point3f[] result = new Point3f[NUM_VERTICES];
		final Transform3D rotation = new Transform3D();
		rotation.rotX(2 * Math.PI / ITERATIONS);
		final Point3f p1 = new Point3f(0, RADIUS, 0);
		final Point3f p2 = new Point3f(length, RADIUS, 0);
		final Point3f p3 = new Point3f(p1);
		final Point3f p4 = new Point3f(p2);
		rotation.transform(p3);
		rotation.transform(p4);

		for (int i = 0; i < ITERATIONS; i++) {
			result[VERTICES_PER_ITERATION * i + 0] = new Point3f(p1);
			result[VERTICES_PER_ITERATION * i + 1] = new Point3f(p2);
			result[VERTICES_PER_ITERATION * i + 2] = new Point3f(p3);
			result[VERTICES_PER_ITERATION * i + 3] = new Point3f(p4);
			rotation.transform(p1);
			rotation.transform(p2);
			rotation.transform(p3);
			rotation.transform(p4);
		}

		return result;
	}

	@SuppressWarnings("unused")
	private static Point3f[] generateCoordinates2(final float length) {
		final Point3f[] result = new Point3f[NUM_VERTICES];
		for (int i = 0; i < ITERATIONS; i++) {
			float x = 0;
			float y = 0;
			float z = 0;
			x = 0;
			z = getZ(i);
			y = getY(i);
			final Point3f p1 = new Point3f(x, y, z);
			x = length;
			final Point3f p2 = new Point3f(x, y, z);

			x = 1;
			z = getZ(i + 1);
			y = getY(i + 1);
			final Point3f p3 = new Point3f(x, y, z);
			x = 0;
			final Point3f p4 = new Point3f(x, y, z);

			result[VERTICES_PER_ITERATION * i + 0] = p1;
			result[VERTICES_PER_ITERATION * i + 1] = p2;
			result[VERTICES_PER_ITERATION * i + 2] = p3;
			result[VERTICES_PER_ITERATION * i + 3] = p4;
		}

		return result;
	}

	private static float getY(final int i) {
		return (float) (RADIUS * Math.sin(2 * Math.PI * i / ITERATIONS));
	}

	private static float getZ(final int i) {
		return (float) (RADIUS * Math.cos(2 * Math.PI * i / ITERATIONS));
	}
}
