package org.ivo.hilbert.grammar;

public class Rule {

	final String leftSide;
	final String rightSide;

	public Rule(final String leftSide, final String rightSide) {
		this.leftSide = leftSide == null ? "" : leftSide.trim();
		this.rightSide = rightSide == null ? "" : rightSide.trim();
	}

	@Override
	public boolean equals(final Object obj) {
		return obj instanceof Rule && ((Rule) obj).leftSide.equals(leftSide);
	}
}