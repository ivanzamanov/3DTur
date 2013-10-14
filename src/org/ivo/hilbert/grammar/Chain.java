package org.ivo.hilbert.grammar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class Chain extends Terminal {

	private final ArrayList<Terminal> terminals = new ArrayList<Terminal>();

	public Chain(final String string) {
		super("");
		final String[] values = string.split(Pattern.quote(" "));
		for (int i = 0; i < values.length; i++) {
			this.terminals.add(new Terminal(values[i]));
		}
	}

	@Override
	public void applyRules(final List<Rule> rules) {
		for (final Terminal terminal : terminals) {
			terminal.applyRules(rules);
		}
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		final Iterator<Terminal> iterator = terminals.iterator();
		assert iterator.hasNext();
		builder.append(iterator.next().toString());
		while (iterator.hasNext()) {
			final Terminal terminal = iterator.next();
			builder.append(" ");
			builder.append(terminal.toString());
		}
		return builder.toString();
	}
}
