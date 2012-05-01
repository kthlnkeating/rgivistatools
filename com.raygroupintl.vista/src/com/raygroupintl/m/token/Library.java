package com.raygroupintl.m.token;

import java.util.List;

import com.raygroupintl.fnds.IToken;

public class Library {
	public static void append(StringBuilder sb, IToken token) {
		if (token !=  null) {
			String value = token.getStringValue();
			sb.append(value);
		}
	}

	public static void append(StringBuilder sb, char ch, IToken token) {
		if (token !=  null) {
			sb.append(ch);
			String value = token.getStringValue();
			sb.append(value);
		}
	}

	public static void append(StringBuilder sb, IToken token, char ch) {
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
	
	public static String getStringValue(IToken[] list) {
		String result = "";
		if (list != null) {
			for (IToken t : list) {
				if (t != null) result += t.getStringValue();
			}			
		}
		return result;
	}

	public static int getStringSize(IToken[] list) {
		int result = 0;
		if (list != null) {
			for (IToken t : list) {
				if (t != null) result += t.getStringSize();
			}			
		}
		return result;
	}

	public static int getStringSize(List<IToken> list) {
		int result = 0;
		if (list != null) {
			for (IToken t : list) {
				result += t.getStringSize();
			}			
		}
		return result;
	}
}
