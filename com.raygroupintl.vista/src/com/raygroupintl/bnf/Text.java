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
	
	public boolean hasNextChar() {
		return this.index+1 < this.text.length();
	}
	
	public char getChar() {
		return this.text.charAt(this.index);
	}
	
	public char getNextChar() {
		return this.text.charAt(this.index+1);
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public void forward() {
		++this.index;
	}
	
	public String getPreviousString(int fromIndex) {
		return this.text.substring(fromIndex, this.index);
	}

	public String getString(int length) {
		return this.text.substring(this.index, this.index + length);
	}

	public boolean startsWith(String value) {
		return this.text.startsWith(value, this.index);		
	}
	
	public String extract(int length) {
		String result = this.text.substring(this.index, this.index+length);
		this.index += length;
		return result;
	}

	public Token extractToken(String value, StringAdapter adapter, boolean ignoreCase) {
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
	
	public Token extractToken(Predicate predicate, CharacterAdapter adapter) {
		if (this.index < this.text.length()) {
			char ch = this.text.charAt(this.index);
			if (predicate.check(ch)) {
				++this.index;
				return adapter.convert(ch);
			}
		}
		return null;		
	}
	
	public Token extractToken(Predicate predicate, StringAdapter adapter) {
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
	
	public Token extractEOLToken() {
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
		Text result = new Text(this.text);
		result.index = this.index;
		return result;
	}
}
