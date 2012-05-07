package com.raygroupintl.m.token;

import java.util.List;

import com.raygroupintl.bnf.Token;

public class Library {
	public static void append(StringBuilder sb, Token token) {
		if (token !=  null) {
			String value = token.getStringValue();
			sb.append(value);
		}
	}

	public static void append(StringBuilder sb, char ch, Token token) {
		if (token !=  null) {
			sb.append(ch);
			String value = token.getStringValue();
			sb.append(value);
		}
	}

	public static void append(StringBuilder sb, Token token, char ch) {
		if (token !=  null) {
			String value = token.getStringValue();
			sb.append(value);
			sb.append(ch);
		}
	}
	
	public static boolean isIdent(char ch) {
		return ((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <='Z'));
	}
	
	public static boolean isDigit(char ch) {
		return (ch >= '0') && (ch <= '9');
	}
	
	public static String getStringValue(Token[] list) {
		String result = "";
		if (list != null) {
			for (Token t : list) {
				if (t != null) result += t.getStringValue();
			}			
		}
		return result;
	}

	public static int getStringSize(Token[] list) {
		int result = 0;
		if (list != null) {
			for (Token t : list) {
				if (t != null) result += t.getStringSize();
			}			
		}
		return result;
	}

	public static int getStringSize(List<Token> list) {
		int result = 0;
		if (list != null) {
			for (Token t : list) {
				result += t.getStringSize();
			}			
		}
		return result;
	}
}
