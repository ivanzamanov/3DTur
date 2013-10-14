package org.ivo.hilbert.grammar;

import java.util.List;

public class Terminal {

	private final String value;
	private Chain delegate = null;

	public Terminal(final String value) {
		this.value = value;
	}

	public void applyRules(final List<Rule> rules) {
		if (delegate != null) {
			delegate.applyRules(rules);
		} else {
			for (final Rule rule : rules) {
				if (getValue().equals(rule.leftSide)) {
					this.delegate = new Chain(rule.rightSide);
					break;
				}
			}
		}
	}

	@Override
	public String toString() {
		if (delegate == null) {
			return getValue();
		} else {
			return delegate.toString();
		}
	}

	public String getValue() {
		return value;
	}
}
