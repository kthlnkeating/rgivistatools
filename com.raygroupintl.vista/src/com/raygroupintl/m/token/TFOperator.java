package com.raygroupintl.m.token;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public class TFOperator implements ITokenFactory {
	private static final char[] OPERATOR_CHARS = {
		'!', '#', '&', '\'', '*', '+', '-', '/', '<', '=', '>', '?', '[', '\\', ']', '_', '|'
	};

	private static Set<String> OPERATORS = new HashSet<String>();
	static {
		String[] ops = {
				"-", "+", "_", "*", "/", "#", "\\", "**", 
				"&", "!", "=", "<", ">", "[", "]", "?", "]]",
				"'&", "'!", "'=", "'<", "'>", "'[", "']", "'?", "']]"
		};
		for (String op : ops) {
			OPERATORS.add(op);
		}; 
	}
	
	public static void addOperator(String op) {
		for (int i=0; i<op.length(); ++i) {
			char ch = op.charAt(i);
			if (Arrays.binarySearch(OPERATOR_CHARS, ch) < 0) {
				throw new IllegalArgumentException();
			}
		}
		OPERATORS.add(op);
	}
	
	@Override
	public IToken tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		int index = fromIndex;
		while (index < endIndex) {
			char ch = line.charAt(index);	
			if (Arrays.binarySearch(OPERATOR_CHARS, ch) < 0) break;
			++index;
		}
		
		for (int i=index; i>fromIndex; --i) {
			String op = line.substring(fromIndex, i);
			if (OPERATORS.contains(op)) {
				return new TOperator(op);
			}
		}
		return null;	
	}
	
	public static TFOperator getInstance() {
		return new TFOperator();
	}
}
