package org.ivo.hilbert;

import java.util.ArrayList;

public class Hilbert3D2 {
	private Hilbert3D2[] childs;
	private final float[] center;

	public Hilbert3D2(final float[] center, final float size, final int depth) {
		this(center, size, depth, 0, 1, 2, 3, 4, 5, 6, 7);
	}

	// ----------------------------------------------------------------------------------------------------
	// generating the hilbert curve.
	// this constructor is called recursively. it generates the vertices for the
	// hilbert curve.
	private Hilbert3D2(final float[] center, final float size, int depth,
			final int a, final int b, final int c, final int d, final int e,
			final int f, final int g, final int h) {
		this.center = center;

		if (depth-- >= 0) {

			final float nx = center[0] - size, ny = center[1] - size, nz = center[2]
					- size;
			final float px = center[0] + size, py = center[1] + size, pz = center[2]
					+ size;

			final float[][] cube_corners = { { nx, py, nz }, { nx, py, pz },
					{ nx, ny, pz }, { nx, ny, nz }, { px, ny, nz },
					{ px, ny, pz }, { px, py, pz }, { px, py, nz } };

			childs = new Hilbert3D2[] {
					new Hilbert3D2(cube_corners[a], size, depth, a, d, e, h, g,
							f, c, b),
					new Hilbert3D2(cube_corners[b], size, depth, a, h, g, b, c,
							f, e, d),
					new Hilbert3D2(cube_corners[c], size, depth, a, h, g, b, c,
							f, e, d),
					new Hilbert3D2(cube_corners[d], size, depth, c, d, a, b, g,
							h, e, f),
					new Hilbert3D2(cube_corners[e], size, depth, c, d, a, b, g,
							h, e, f),
					new Hilbert3D2(cube_corners[f], size, depth, e, d, c, f, g,
							b, a, h),
					new Hilbert3D2(cube_corners[g], size, depth, e, d, c, f, g,
							b, a, h),
					new Hilbert3D2(cube_corners[h], size, depth, g, f, c, b, a,
							d, e, h) };
		}
	}

	// ----------------------------------------------------------------------------------------------------
	// get the ordered vertice list
	public final void getVertices(final ArrayList<float[]> vertices, int depth) {
		if (childs == null || depth-- == 0) {
			vertices.add(center);
		} else {
			for (final Hilbert3D2 h : childs) {
				h.getVertices(vertices, depth);
			}
		}
	}

	public final ArrayList<float[]> getVertices(final int depth) {
		final ArrayList<float[]> vertices = new ArrayList<float[]>();
		getVertices(vertices, depth);
		return vertices;
	}
}