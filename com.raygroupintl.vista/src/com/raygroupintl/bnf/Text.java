package com.raygroupintl.bnf;

import com.raygroupintl.charlib.Predicate;

public class Text {
	private String text;
	private int index;
	
	public Text(String text) {
		this.text = text;
	}
	
	public Text(String text, int index) {
		this.text = text;
		this.index = index;
	}
	
	public boolean onEndOfText() {
		return this.index == this.text.length();
	}
	
	public boolean onChar() {
		return this.index < this.text.length();
	}
	
	public char getChar() {
		return this.text.charAt(this.index);
	}
	
	public boolean onChar(int forward) {
		return this.index+forward < this.text.length();
	}
	
	public char getChar(int forward) {
		return this.text.charAt(this.index+forward);
	}
	
	Token extractToken(String value, StringAdapter adapter, boolean ignoreCase) {
		if (ignoreCase) {
			String piece = this.text.substring(this.index, this.index+value.length());
			if (piece.equalsIgnoreCase(value)) {
				this.index += value.length();
				return adapter.convert(piece);
			}
		} else {
			if (this.text.startsWith(value, this.index)) {
				String piece = this.text.substring(this.index, this.index+value.length());
				this.index += value.length();
				return adapter.convert(piece);
			}
		}
		return null;	
	}
	
	Token extractToken(Predicate predicate, CharacterAdapter adapter) {
		if (this.index < this.text.length()) {
			char ch = this.text.charAt(this.index);
			if (predicate.check(ch)) {
				++this.index;
				return adapter.convert(ch);
			}
		}
		return null;		
	}
	
	Token extractToken(Predicate predicate, StringAdapter adapter) {
		int fromIndex = this.index;
		while (this.onChar()) {
			char ch = this.getChar();
			if (! predicate.check(ch)) {
				if (fromIndex == this.index) {
					return null;
				} else {
					return adapter.convert(this.text.substring(fromIndex, this.index));
				}
			}
			++this.index;
		}
		if (fromIndex < this.text.length()) {
			return adapter.convert(this.text.substring(fromIndex, this.index));			
		} else {
			return null;
		}
	}
	
	public Token extractToken(int length, StringAdapter adapter) {
		Token result = adapter.convert(this.text.substring(this.index, this.index+length));
		this.index += length;
		return result;
	}
	
	Token extractEOLToken() {
		if (this.onChar()) {
			char ch0th = this.getChar();
			if ((ch0th == '\n') || (ch0th == '\r')) {
				++this.index;
				if (this.onChar()) {
					char ch1st = this.getChar();
					if ((ch1st == '\n') || (ch1st == '\r')) {
						++this.index;
						return new TString(this.text.substring(this.index-2, this.index));
					}
				}
				return new TString(this.text.substring(this.index-1, this.index));
			}
		}
		return null;		
	}
	
	public Text getCopy() {
		return new Text(this.text, this.index);
	}
}
