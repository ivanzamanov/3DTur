package org.ivo.hilbert;

import java.awt.Color;
import java.io.File;

import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.TransformGroup;
import javax.swing.JApplet;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import org.ivo.hilbert.grammar.LSystemGrammar;
import org.ivo.hilbert.turtle.Turtle3D;
import org.ivo.hilbert.turtle.TurtleConfig;
import org.ivo.hilbert.turtle.TurtleFactory;
import org.ivo.hilbert.utils.Utils;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Hilbert3D extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Canvas3D c1 = new Canvas3D(
			SimpleUniverse.getPreferredConfiguration());

	@Override
	public void init() {
		super.init();
		this.setSize(900, 900);
		final SimpleUniverse universe = new SimpleUniverse(c1);
		c1.setSize(900, 900);
		add(c1);
		try {
			createSceneGraph(universe);
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void createSceneGraph(final SimpleUniverse universe)
			throws Exception {
		final BranchGroup graphRoot = new BranchGroup();
		addBackground(graphRoot);
		addLights(graphRoot);
		final TransformGroup interactionTransformGroup = addInteraction(graphRoot);
		Utils.addAxes(interactionTransformGroup);
		final TransformGroup pathTransformGroup = new TransformGroup();
		interactionTransformGroup.addChild(pathTransformGroup);

		final File file = new File(".." + File.separator + "data", "TestRules");
		final LSystemGrammar grammer = new LSystemGrammar(file);
		final String turtleString = grammer.apply();
		final Turtle3D turtle = TurtleFactory.createTurtle(turtleString);
		turtle.interpret(pathTransformGroup, new TurtleConfig());
		turtle.adjustView(universe.getViewingPlatform(), pathTransformGroup);

		graphRoot.compile();

		universe.addBranchGraph(graphRoot);
	}

	private void addBackground(final BranchGroup graphRoot) {
		final Background sceneBackground = new Background();
		final Bounds bounds = new BoundingSphere(new Point3d(0d, 0d, 0d), 150d);
		sceneBackground.setApplicationBounds(bounds);
		sceneBackground.setColor(new Color3f(Color.gray));
		graphRoot.addChild(sceneBackground);
	}

	private TransformGroup addInteraction(final BranchGroup graphRoot) {
		final TransformGroup interactionTransform = new TransformGroup();
		interactionTransform
				.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		interactionTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		graphRoot.addChild(interactionTransform);

		final MouseRotate mouseRotate = new MouseRotate(interactionTransform);

		final BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 0),
				150);
		mouseRotate.setSchedulingBounds(bounds);

		final MouseWheelZoom mouseZoom = new MouseWheelZoom();
		mouseZoom.setTransformGroup(interactionTransform);
		mouseZoom.setSchedulingBounds(bounds);

		graphRoot.addChild(mouseRotate);
		graphRoot.addChild(mouseZoom);
		return interactionTransform;
	}

	private void addLights(final BranchGroup graphRoot) {
		final Color3f lightColor = new Color3f(Color.gray);
		final Vector3f lightDirection = new Vector3f(4.0f, -7.0f, -12.0f);

		final DirectionalLight light1 = new DirectionalLight(lightColor,
				lightDirection);

		final BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0,
				0.0), 100.0);
		light1.setInfluencingBounds(bounds);

		graphRoot.addChild(light1);
	}
}
