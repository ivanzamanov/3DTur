package org.ivo.hilbert.turtle;

import java.util.HashMap;
import java.util.regex.Pattern;

import org.ivo.hilbert.turtle.path.PipeSegment;
import org.ivo.hilbert.turtle.path.PitchNegative;
import org.ivo.hilbert.turtle.path.PitchPositive;
import org.ivo.hilbert.turtle.path.ReverseTransform;
import org.ivo.hilbert.turtle.path.RollNegative;
import org.ivo.hilbert.turtle.path.RollPositive;
import org.ivo.hilbert.turtle.path.YawNegative;
import org.ivo.hilbert.turtle.path.YawPositive;

public class TurtleFactory {

	private TurtleFactory() {

	}

	private static final HashMap<String, Class<? extends PathSegment>> classMap = generateClassMap();

	private static HashMap<String, Class<? extends PathSegment>> generateClassMap() {
		final HashMap<String, Class<? extends PathSegment>> result = new HashMap<String, Class<? extends PathSegment>>();

		result.put("F", PipeSegment.class);

		result.put("P+", PitchPositive.class);
		result.put("P-", PitchNegative.class);

		result.put("Y+", YawPositive.class);
		result.put("Y-", YawNegative.class);

		result.put("R+", RollPositive.class);
		result.put("R-", RollNegative.class);

		result.put("R", ReverseTransform.class);

		return result;
	}

	public static String createReplacementString(final String string) {
		String currentString = string;
		currentString = currentString.replaceAll(Pattern.quote("|"), "R");

		currentString = currentString.replaceAll(Pattern.quote("+"), "Y+");
		currentString = currentString.replaceAll(Pattern.quote("-"), "Y-");

		currentString = currentString.replaceAll(Pattern.quote("^"), "P+");
		currentString = currentString.replaceAll(Pattern.quote("&"), "P-");

		currentString = currentString.replaceAll(Pattern.quote("<"), "R+");
		currentString = currentString.replaceAll(Pattern.quote(">"), "R-");

		return currentString;
	}

	public static PathSegment createSegment(final String code) {
		final Class<? extends PathSegment> segmentClass = classMap.get(code);
		if (segmentClass == null) {
			return new EmptySegment();
		}
		PathSegment result = null;
		try {
			result = segmentClass.newInstance();
		} catch (final InstantiationException e) {
			e.printStackTrace();
			return new EmptySegment();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
			return new EmptySegment();
		}
		return result;
	}

	public static Turtle3D createTurtle(final String string) {
		final String pathString = TurtleFactory.createReplacementString(string);
		final Turtle3D result = new Turtle3D();
		final String[] segments = pathString.split(" ");
		for (int i = 0; i < segments.length; i++) {
			result.append(createSegment(segments[i]));
		}
		return result;
	}
}
