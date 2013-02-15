package org.ivo.hilbert.turtle.path;

import javax.media.j3d.Transform3D;

import org.ivo.hilbert.turtle.TurtleTransform;

public class ReverseTransform extends TurtleTransform {

	public ReverseTransform() {
		final Transform3D transform = new Transform3D();
		transform.rotX(Math.PI);
		setTransform(transform);
	}
}
