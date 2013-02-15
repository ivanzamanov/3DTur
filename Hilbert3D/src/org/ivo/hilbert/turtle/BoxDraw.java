package org.ivo.hilbert.turtle;

import java.util.Iterator;
import java.util.List;

import javax.media.j3d.Group;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;

public class BoxDraw implements ITurtleDrawProxy {

	@Override
	public void draw(List<Vector3f> vertices, Group target,
			List<Transform3D> rotations, TurtleConfig config) {

		Iterator<Vector3f> verticesIterator = vertices.iterator();
		Iterator<Transform3D> transformsIterator = rotations.iterator();
		Vector3f previous;
		Vector3f next = verticesIterator.next();
		Transform3D transform;
		while (verticesIterator.hasNext()) {
			previous = next;
			next = verticesIterator.next();
			transform = transformsIterator.next();
			transform.setTranslation(previous);
			
			final TransformGroup turtleHeadingTransformGroup = new TransformGroup(
					transform);

			transform.setIdentity();
			float singleBoxLength = config.getSegmentLength() / 2;
			final Vector3f boxTranslation = new Vector3f(singleBoxLength, 0f,
					0f);
			transform.setTranslation(boxTranslation);
			final TransformGroup boxCenteringTransformGroup = new TransformGroup(
					transform);
			float boxWidth = 0.02f;
			final Box box = new Box(singleBoxLength, boxWidth, boxWidth,
					config.getAppearance());
			boxCenteringTransformGroup.addChild(box);
			turtleHeadingTransformGroup.addChild(boxCenteringTransformGroup);
			target.addChild(turtleHeadingTransformGroup);
		}
	}

	@Override
	public void draw(List<Vector3f> vertices, Group target, TurtleConfig config) {
		throw new UnsupportedOperationException("Unsupported");
	}
}
