package org.ivo.hilbert.turtle;

import java.util.List;

import javax.media.j3d.TransformGroup;


public class EmptySegment extends PathSegment {

	protected EmptySegment() {
		super(PathSegmentType.EMPTY);
	}

	@Override
	public TransformGroup interpret(final TransformGroup target,
			final List<PathSegment> previousSegments, TurtleConfig config) {
		return target;
	}

}
