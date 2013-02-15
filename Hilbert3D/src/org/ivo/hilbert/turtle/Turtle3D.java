package org.ivo.hilbert.turtle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Matrix3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.ivo.hilbert.utils.Utils;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class Turtle3D extends PathSegment {

	private final ArrayList<PathSegment> path;
	private TurtleConfig config;
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
	public TransformGroup interpret(final TransformGroup target,
			final List<PathSegment> previousSegments, final TurtleConfig config) {
		this.config = config;

		final List<Vector3f> allPoints = new ArrayList<Vector3f>(
				path.size() / 3);
		final List<Transform3D> transforms = new ArrayList<Transform3D>(
				path.size() / 3);

		final Vector3f tempPoint = new Vector3f();
		allPoints.add(new Vector3f(tempPoint));

		final TransformGroup mainAxesGroup = new TransformGroup();
		Utils.addAxes(mainAxesGroup, 10f);
		target.addChild(mainAxesGroup);

		final Matrix3f turtleCoordinateSystem = new Matrix3f();
		turtleCoordinateSystem.setIdentity();

		tempPoint.set(1, 0, 0);
		Transform3D currentTransform = new Transform3D();

		final Matrix3f tempMatrix = new Matrix3f();

		for (int i = 0; i < path.size(); i++) {
			final PathSegment segment = path.get(i);
			if (segment.getType().equals(PathSegmentType.TRANSFORM)) {
				final TurtleTransform turtleTransform = (TurtleTransform) segment;
				final Transform3D transform = turtleTransform.getTransform();
				currentTransform.mul(transform);
			} else if (segment.getType().equals(PathSegmentType.BODY)) {
				currentTransform.get(tempMatrix);
				turtleCoordinateSystem.mul(tempMatrix);
				assert turtleCoordinateSystem.determinant() == 1;
				final Vector3f previousPoint = allPoints
						.get(allPoints.size() - 1);
				final Vector3f nextPoint = new Vector3f(previousPoint);
				turtleCoordinateSystem.getColumn(0, tempPoint);
				tempPoint.scale(config.getSegmentLength());
				assert tempPoint.length() == config.getSegmentLength();
				nextPoint.add(tempPoint);
				allPoints.add(nextPoint);
				transforms.add(currentTransform);
				currentTransform = new Transform3D(currentTransform);
				turtleCoordinateSystem.setIdentity();
			}
		}

		this.pathPoints = allPoints;
		final Iterator<Vector3f> pointsIterator = pathPoints.iterator();
		final Iterator<Transform3D> transformsIterator = transforms.iterator();
		assert pointsIterator.hasNext();
		Vector3f previous = pointsIterator.next();
		while (pointsIterator.hasNext()) {
			final Vector3f next = pointsIterator.next();
			draw(previous, next, target, transformsIterator.next());
			previous = next;
		}
		assert !transformsIterator.hasNext();

		return target;
	}

	private void draw(final Vector3f previous, final Vector3f next,
			final TransformGroup target, final Transform3D rotationTransform) {

		rotationTransform.setTranslation(previous);
		final TransformGroup turtleHeadingTransformGroup = new TransformGroup(
				rotationTransform);

		rotationTransform.setIdentity();
		float singleBoxLength = config.getSegmentLength() / 2;
		final Vector3f boxTranslation = new Vector3f(singleBoxLength, 0f, 0f);
		rotationTransform.setTranslation(boxTranslation);
		final TransformGroup boxCenteringTransformGroup = new TransformGroup(
				rotationTransform);
		float boxWidth = 0.02f;
		final Box box = new Box(singleBoxLength, boxWidth, boxWidth,
				config.getAppearance());
		boxCenteringTransformGroup.addChild(box);
		turtleHeadingTransformGroup.addChild(boxCenteringTransformGroup);
		target.addChild(turtleHeadingTransformGroup);
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
