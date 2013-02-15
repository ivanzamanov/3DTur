package org.ivo.hilbert.turtle;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

public abstract class TurtleTransform extends PathSegment {

	protected TurtleTransform() {
		super(PathSegmentType.TRANSFORM);
	}

	private Transform3D transform;

	protected void setTransform(final Transform3D transform) {
		this.transform = transform;
	}

	public Transform3D getTransform() {
		return transform;
	}

	@Override
	public final TransformGroup interpret(final TransformGroup target, TurtleConfig config) {
		final TransformGroup newGroup = new TransformGroup(transform);
		target.addChild(newGroup);
		return newGroup;
	}
}
