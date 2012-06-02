package com.raygroupintl.parser;

import java.util.ArrayList;
import java.util.List;

public class TString implements Token {
	private StringPiece value;
		
	public TString(StringPiece value) {
		this.value = value;
	}
	
	public TString(String data, int beginIndex, int endIndex) {
		this.value = new StringPiece(data, beginIndex, endIndex);
	}
	
	@Override
	public StringPiece toValue() {
		return this.value;
	}
	
	@Override
	public List<Token> toList() {	
		List<Token> result = new ArrayList<Token>();
		result.add(this);	
		return result;
	}

	public void setValue(StringPiece value) {
		this.value = value;
	}

	@Override
	public void beautify() {		
	}
}
