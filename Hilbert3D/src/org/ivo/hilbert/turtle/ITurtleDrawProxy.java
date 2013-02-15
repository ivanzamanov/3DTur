package org.ivo.hilbert.turtle;

import java.util.List;

import javax.media.j3d.Group;
import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3f;

public interface ITurtleDrawProxy {
	/**
	 * 
	 * @param vertices
	 *            Starting point
	 * @param target
	 *            Target to draw on.
	 * @param rotations
	 *            For convenience - the rotation from "from" to "to".
	 * @param config
	 *            TODO
	 */
	public void draw(List<Vector3f> vertices, Group target,
			List<Transform3D> rotations, TurtleConfig config)
			throws UnsupportedOperationException;

	/**
	 * 
	 * @param vertices
	 *            Starting point
	 * @param target
	 *            Target to draw on.
	 * @param config
	 *            TODO
	 */
	public void draw(List<Vector3f> vertices, Group target, TurtleConfig config);
}
