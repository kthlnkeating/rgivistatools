package com.raygroupintl.bnf;

public class Text {
	private String text;
	private int index;
	
	public Text(String text) {
		this.text = text;
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

	public String extract(String value, boolean ignoreCase) {
		if (ignoreCase) {
			String piece = this.text.substring(this.index, this.index+value.length());
			if (piece.equalsIgnoreCase(value)) {
				this.index += value.length();
				return piece;
			}
		} else {
			if (this.text.startsWith(value, this.index)) {
				this.index += value.length();
				return value;
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
