package org.ivo.hilbert.turtle.path;

import javax.media.j3d.Transform3D;

import org.ivo.hilbert.turtle.TurtleTransform;

public class YawPositive extends TurtleTransform {

	public YawPositive() {
		final Transform3D transform = new Transform3D();
		transform.rotZ(Math.PI / 2);
		setTransform(transform);
	}
}
