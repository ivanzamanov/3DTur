package org.ivo.hilbert.turtle.path;

import javax.media.j3d.Transform3D;

import org.ivo.hilbert.turtle.TurtleTransform;

public class RollNegative extends TurtleTransform {

	public RollNegative() {
		final Transform3D transform = new Transform3D();
		transform.rotX(-angle);
		setTransform(transform);
	}
}
