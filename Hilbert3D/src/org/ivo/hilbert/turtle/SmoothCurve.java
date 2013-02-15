package org.ivo.hilbert.turtle;

import java.util.Iterator;
import java.util.List;

import javax.media.j3d.Group;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;

public class SmoothCurve implements ITurtleDrawProxy {

	private final int iTERATIONS = 20;
	private final float STEP = 1f / iTERATIONS;

	@Override
	public void draw(List<Vector3f> vertices, Group target, List<Transform3D> rotations,
			TurtleConfig config) {
		this.draw(vertices, target, config);
	}

	@Override
	public void draw(List<Vector3f> vertices, Group target, TurtleConfig config) {
		Iterator<Vector3f> verticesIterator = vertices.iterator();
		Vector3f pPrev = verticesIterator.next();
		Vector3f prev = verticesIterator.next();
		Vector3f next = verticesIterator.next();
		
		Vector3f mid1 = getMid(pPrev, prev);
		Vector3f mid2 = getMid(prev, next);
		drawCurve(mid1, prev, mid2, target, config);
		
		while (verticesIterator.hasNext()) {
			pPrev = prev;
			prev = next;
			next = verticesIterator.next();

			mid1 = getMid(pPrev, prev);
			mid2 = getMid(prev, next);
			drawCurve(mid1, prev, mid2, target, config);
		}
	}

	private Vector3f getMid(Vector3f p1, Vector3f p2) {
		Vector3f result = new Vector3f(p1);
		result.add(p2);
		result.scale(0.5f);
		return result;
	}

	private void drawCurve(Vector3f from, Vector3f control, Vector3f to, Group target, TurtleConfig config) {
		
		Transform3D transform = new Transform3D();
		Vector3f tmp = new Vector3f();
		Vector3f pPrevSave = new Vector3f();
		Vector3f prevSave = new Vector3f();
		Vector3f nextSave = new Vector3f();
		
		for (float i=0; i<iTERATIONS; i++) {
			float t = i * STEP;
			pPrevSave.set(from);
			prevSave.set(control);
			nextSave.set(to);
			
			float t1 = 1f - t;
			pPrevSave.scale(t1 * t1);
			System.out.println(t1 * t1);
			prevSave.scale(2 * t1 * t);
			System.out.println(2 * t1 * t);
			nextSave.scale(t * t);
			System.out.println(t * t);
			tmp.set(0,0,0);
			tmp.add(pPrevSave);
			tmp.add(prevSave);
			tmp.add(nextSave);
			
			transform.setTranslation(tmp);
			
			final TransformGroup turtleHeadingTransformGroup = new TransformGroup(
					transform);

			transform.setIdentity();
			float boxDim = 0.01f;
			final Vector3f boxTranslation = new Vector3f(boxDim, 0f,
					0f);
			transform.setTranslation(boxTranslation);
			final TransformGroup boxCenteringTransformGroup = new TransformGroup(
					transform);
			final Box box = new Box(boxDim, boxDim, boxDim,
					config.getAppearance());
			boxCenteringTransformGroup.addChild(box);
			turtleHeadingTransformGroup.addChild(boxCenteringTransformGroup);
			target.addChild(turtleHeadingTransformGroup);
		}
	}
}
