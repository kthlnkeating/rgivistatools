package com.raygroupintl.bnf;


public class TFConstString extends TokenFactory {
	private String value;
	private boolean ignoreCase;
	private CharactersAdapter adapter;
	
	public TFConstString(String value) {
		this.value = value;
		this.ignoreCase = false;
	}
	
	public TFConstString(String value, boolean ignoreCase) {
		this.value = value;
		this.ignoreCase = ignoreCase;
	}

	public void setAdapter(CharactersAdapter adapter) {
		this.adapter = adapter;
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
				return new TCharacters(result);
			} else {
				return this.adapter.convert(result);
			}
		} else {
			return null;
		}
	}
}
