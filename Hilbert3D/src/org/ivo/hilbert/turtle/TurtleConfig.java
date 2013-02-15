package org.ivo.hilbert.turtle;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.vecmath.Color3f;

public class TurtleConfig {

	private Appearance appearance;
	private float segmentLength;
	private boolean saveTransforms;
	private ITurtleDrawProxy drawProxy;

	public TurtleConfig() {
		this.setAppearance(createDefaultAppearance());
		this.setSegmentLength(0.3f);
		this.setSaveTransforms(true);
		this.setDrawProxy(new BoxDrawProxy());
	}

	public Appearance getAppearance() {
		return appearance;
	}

	private static Appearance createDefaultAppearance() {
		final Appearance defaultAppearance = new Appearance();
		final Color3f specularColor = new Color3f(Color.black);
		final Color3f diffuseColor = new Color3f(Color.lightGray);
		final Color3f emissiveColor = new Color3f(Color.black);
		final Color3f ambientColor = new Color3f(Color.black);
		final Material pathMaterial = new Material(ambientColor, emissiveColor,
				diffuseColor, specularColor, 0);
		defaultAppearance.setMaterial(pathMaterial);
		return defaultAppearance;
	}

	public void setAppearance(final Appearance appearance) {
		this.appearance = appearance;
	}

	public float getSegmentLength() {
		return segmentLength;
	}

	public void setSegmentLength(final float segmentLength) {
		this.segmentLength = segmentLength;
	}

	public boolean getSaveTransforms() {
		return saveTransforms;
	}

	public void setSaveTransforms(boolean saveTransforms) {
		this.saveTransforms = saveTransforms;
	}

	public ITurtleDrawProxy getDrawProxy() {
		return drawProxy;
	}

	public void setDrawProxy(ITurtleDrawProxy drawProxy) {
		this.drawProxy = drawProxy;
	}
}
