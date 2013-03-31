package org.ivo.hilbert.turtle.path;

import javax.media.j3d.Transform3D;

import org.ivo.hilbert.turtle.TurtleTransform;

public class PitchNegative extends TurtleTransform {

	public PitchNegative() {
		final Transform3D transform = new Transform3D();
		transform.rotY(-angle);
		setTransform(transform);
	}
}
