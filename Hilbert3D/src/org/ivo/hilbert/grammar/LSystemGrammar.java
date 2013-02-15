package org.ivo.hilbert.grammar;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class LSystemGrammar {

	private final ArrayList<Rule> rulesList;
	private String startingString;
	private int iterations;

	public LSystemGrammar(final File file) throws Exception {
		this(new FileInputStream(file));
	}

	public LSystemGrammar(final InputStream is) throws Exception {
		this.rulesList = parseRules(is);
	}

	private ArrayList<Rule> parseRules(final InputStream is) throws Exception {
		final ArrayList<Rule> result = new ArrayList<Rule>();
		final Scanner scanner = new Scanner(is);
		scanner.useDelimiter(Pattern.quote("\n"));
		final String[] params = scanner.next().split(Pattern.quote(" "));
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < params.length - 1; i++) {
			builder.append(params[i].trim());
			builder.append(" ");
		}
		startingString = builder.toString();
		iterations = Integer.parseInt(params[params.length - 1].trim());

		while (scanner.hasNext()) {
			final String line = scanner.next();
			final String[] values = line.split(Pattern.quote("->"));
			if (values.length != 2) {
				scanner.close();
				throw new Exception("Invalid Rule Format");
			}
			final String leftSide = values[0].trim();
			final String rightSide = values[1].trim();
			result.add(new Rule(leftSide, rightSide));
		}
		scanner.close();
		return result;
	}

	public String apply() {
		String currentString = startingString;

		System.out.println("Starting axiom: " + startingString);

		final Chain chain = new Chain(currentString);

		for (int i = 0; i < iterations; i++) {
			final List<Rule> applicableRules = findApplicableRules(rulesList,
					currentString);

			chain.applyRules(applicableRules);

			currentString = chain.toString();

			System.out.println("At iteration " + (i + 1));
			System.out.println(currentString);
		}
		return currentString;
	}

	private static List<Rule> findApplicableRules(final List<Rule> rulesList,
			final String string) {
		synchronized (rulesList) {
			final LinkedList<Rule> result = new LinkedList<Rule>();

			final Iterator<Rule> it = rulesList.iterator();
			while (it.hasNext()) {
				final Rule rule = it.next();
				if (string.contains(rule.leftSide) && !result.contains(rule)) {
					result.add(rule);
				}
			}

			return result;
		}
	}
}
