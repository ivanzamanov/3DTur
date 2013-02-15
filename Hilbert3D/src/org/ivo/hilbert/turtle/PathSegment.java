package org.ivo.hilbert.turtle;

import java.util.List;

import javax.media.j3d.TransformGroup;

public abstract class PathSegment {

	private final PathSegmentType type;

	protected PathSegment(final PathSegmentType type) {
		this.type = type;
	}

	public PathSegmentType getType() {
		return type;
	}

	public abstract TransformGroup interpret(TransformGroup target,
			List<PathSegment> previousSegments, TurtleConfig config);

	static enum PathSegmentType {
		TRANSFORM, BODY, EMPTY
	}
}
