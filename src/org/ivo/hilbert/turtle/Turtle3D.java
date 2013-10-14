package org.ivo.hilbert.turtle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.universe.ViewingPlatform;

public class Turtle3D extends PathSegment {

	private final ArrayList<PathSegment> path;
	private List<Vector3f> pathPoints;

	public Turtle3D() {
		super(PathSegmentType.EMPTY);
		this.path = new ArrayList<PathSegment>();
	}

	public void clear() {
		path.clear();
	}

	public void append(final PathSegment segment) {
		path.add(segment);
	}

	@Override
	public TransformGroup interpret(final TransformGroup target) {

		// Will hold the ordered list of vertices
		final List<Vector3f> allPoints = new ArrayList<Vector3f>(
				path.size() / 3);

		// If we need to pass the transforms between adjecent vertices to the
		// drawing delegate.
		final List<Transform3D> transforms = new ArrayList<Transform3D>(
				path.size() / 3);

		// Will reuse this object.
		final Vector3f tempPoint = new Vector3f();

		// Start at (0,0).
		allPoints.add(new Vector3f(tempPoint));

		// The current coordinate system of the turtle - rotations only.
		final Matrix3f turtleCoordinateSystem = new Matrix3f();
		turtleCoordinateSystem.setIdentity();

		// The second point will be at (1,0,0) unless some transformations are
		// applied.
		tempPoint.set(1, 0, 0);

		// Will hold the rotations from the last vertex to the next.
		Transform3D currentTransform = new Transform3D();

		// Reuse this.
		final Matrix3f tempMatrix = new Matrix3f();

		for (int i = 0; i < path.size(); i++) {
			final PathSegment segment = path.get(i);
			if (segment.getType().equals(PathSegmentType.TRANSFORM)) {
				// Transformation, i.e. rotation around one of the axes.
				final TurtleTransform turtleTransform = (TurtleTransform) segment;
				final Transform3D transform = turtleTransform.getTransform();

				// Simply multiply the current rotation, i.e. append to it.
				currentTransform.mul(transform);
			} else if (segment.getType().equals(PathSegmentType.BODY)) {
				// Move forward.

				// Get the rotation only, the distance between vertices is held
				// in the configuration object.
				currentTransform.get(tempMatrix);
				// Rotate the turtle
				turtleCoordinateSystem.mul(tempMatrix);
				final Vector3f previousPoint = allPoints
						.get(allPoints.size() - 1);
				// Start at the last point.
				final Vector3f nextPoint = new Vector3f(previousPoint);
				// Move along the current X axis by the length of a single
				// segment.
				turtleCoordinateSystem.getColumn(0, tempPoint);
				tempPoint.scale(TurtleConfig.segmentLength);
				// And translate by the previous point.
				nextPoint.add(tempPoint);

				// Append to the list of vertices, and if needed - append the
				// rotation transform between these two vertices.
				allPoints.add(nextPoint);
				transforms.add(currentTransform);
				currentTransform = new Transform3D(currentTransform);

				// Reset the turtle's coordinate system.
				turtleCoordinateSystem.setIdentity();
			}
		}

		this.pathPoints = allPoints;
		final ITurtleDrawProxy proxy = TurtleConfig.drawProxy;
		try {
			proxy.draw(allPoints, target);
		} catch (final UnsupportedOperationException e) {
			proxy.draw(allPoints, target, transforms);
		}

		return target;
	}

	public void adjustView(final ViewingPlatform viewingPlatform,
			final TransformGroup targetTransformGroup) {
		// First, calculate the maximum bounds of the path.
		float maxX = 0;
		float maxY = 0;
		float maxZ = 0;
		float minX = 0;
		float minY = 0;
		float minZ = 0;

		final Iterator<Vector3f> iterator = pathPoints.iterator();
		while (iterator.hasNext()) {
			final Vector3f point = iterator.next();
			if (point.x > maxX) {
				maxX = point.x;
			} else if (point.x < minX) {
				minX = point.x;
			}
			if (point.y > maxY) {
				maxY = point.y;
			} else if (point.y < minY) {
				minY = point.y;
			}
			if (point.z > maxZ) {
				maxZ = point.z;
			} else if (point.z < minZ) {
				minZ = point.z;
			}
		}

		final Transform3D transform = new Transform3D();
		final Vector3f vector = new Vector3f();

		// Now calculate the view transform's Z so the entire path will fit onto
		// the screen.
		float maxDimension = Math.max(maxX - minX, maxY - minY);
		maxDimension *= 1.5;
		final float zTransform = maxDimension + maxZ + 1;
		vector.set(0, 0, zTransform);
		transform.setTranslation(vector);

		viewingPlatform.getViewPlatformTransform().setTransform(transform);

		// Lastly, translate the path so it's center will be at (0,0,0).
		targetTransformGroup.getTransform(transform);
		float xAdjust = 0, yAdjust = 0, zAdjust = 0;
		xAdjust = -(maxX + minX) / 2;
		// xAdjust *= maxX > Math.abs(minX) ? 1 : -1;

		yAdjust = -(maxY + minY) / 2;
		// yAdjust *= maxY > Math.abs(minY) ? 1 : -1;

		zAdjust = -(maxZ + minZ) / 2;
		// zAdjust *= maxZ > Math.abs(minZ) ? 1 : -1;

		vector.set(xAdjust, yAdjust, zAdjust);
		final Transform3D tempTransform = new Transform3D();
		tempTransform.setTranslation(vector);
		transform.mul(tempTransform);
		targetTransformGroup.setTransform(transform);
	}
}
