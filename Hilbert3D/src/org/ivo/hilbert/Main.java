package org.ivo.hilbert;

import java.applet.Applet;

import javax.swing.WindowConstants;

import com.sun.j3d.utils.applet.JMainFrame;

public class Main {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Applet applet = new Hilbert3D();
		final JMainFrame frame = new JMainFrame(applet, 900, 900);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.run();
		// iterate(3);
	}

	public static String iterate(final int maxLength) {
		final String m_axiom = "L";
		String m_tree = new String(m_axiom);
		final char m_alphabet[] = new char[2];
		m_alphabet[0] = 'L';
		m_alphabet[1] = 'R';
		final String[] m_rule = new String[2];
		m_rule[0] = "+ R F - L F L - F R +";
		m_rule[1] = "- L F + R F R + F L -";
		final int[] ruleLen = new int[m_alphabet.length];
		for (int j = 0; j < m_alphabet.length; j++) {
			ruleLen[j] = m_rule[j].length();
		}
		for (int num = 0; num < maxLength; num++) {
			final int len = m_tree.length();
			int newLen = 0;
			for (int i = 0; i < len; i++) {
				final char c = m_tree.charAt(i);
				for (int j = 0; j < m_alphabet.length; j++) {
					if (c == m_alphabet[j]) {
						newLen += ruleLen[j];
						break;
					}
				}
			}

			final StringBuffer newTree = new StringBuffer(newLen);
			for (int i = 0; i < len; i++) {
				final char c = m_tree.charAt(i);
				for (int j = 0; j < m_alphabet.length; j++) {
					if (c == m_alphabet[j]) {
						newTree.append(m_rule[j]);
						break;
					}
				}
			}
			m_tree = newTree.toString();
			System.out.println(m_tree);
		}
		return m_tree;
	}
}
