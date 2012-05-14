package com.raygroupintl.bnf;


public class TFConstant extends TokenFactory {
	private String value;
	private boolean ignoreCase;
	private StringAdapter adapter;
	
	public TFConstant(String value, StringAdapter adapter) {
		this.value = value;
		this.adapter = adapter;
		this.ignoreCase = false;
	}
	
	public TFConstant(String value, StringAdapter adapter, boolean ignoreCase) {
		this.value = value;
		this.adapter = adapter;
		this.ignoreCase = ignoreCase;
	}

	private String getMatched(String line, int fromIndex) {
		if (this.ignoreCase) {
			String piece = line.substring(fromIndex, fromIndex+this.value.length());
			if (piece.equalsIgnoreCase(this.value)) {
				return piece;
			}
		} else {
			if (line.startsWith(this.value, fromIndex)) {
				return this.value;
			}
		}
		return null;
	}
	
	@Override
	public Token tokenize(String line, int fromIndex) {
		String result = this.getMatched(line, fromIndex);
		if (result != null) {
			if (this.adapter == null) {
				return new TString(result);
			} else {
				return this.adapter.convert(result);
			}
		} else {
			return null;
		}
	}
}
