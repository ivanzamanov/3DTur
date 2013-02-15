package org.ivo.hilbert.turtle.path;

import javax.media.j3d.Transform3D;

import org.ivo.hilbert.turtle.TurtleTransform;

public class RollPositive extends TurtleTransform {

	public RollPositive() {
		final Transform3D transform = new Transform3D();
		transform.rotX(Math.PI / 2);
		setTransform(transform);
	}
}
