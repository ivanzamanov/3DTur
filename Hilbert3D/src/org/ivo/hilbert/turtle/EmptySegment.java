package org.ivo.hilbert.turtle;

import javax.media.j3d.TransformGroup;


public class EmptySegment extends PathSegment {

	protected EmptySegment() {
		super(PathSegmentType.EMPTY);
	}

	@Override
	public TransformGroup interpret(final TransformGroup target, TurtleConfig config) {
		return target;
	}

}
