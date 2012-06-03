package com.raygroupintl.parser;

import java.util.ArrayList;
import java.util.List;

public class TString extends StringPiece implements Token {
	public TString() {
		super();
	}

	public TString(StringPiece value) {
		super(value);
	}
	
	public TString(String data, int beginIndex, int endIndex) {
		super(data, beginIndex, endIndex);
	}
	
	@Override
	public StringPiece toValue() {
		return this;
	}
	
	@Override
	public List<Token> toList() {	
		List<Token> result = new ArrayList<Token>();
		result.add(this);	
		return result;
	}

	public void setValue(StringPiece value) {
		super.set(value);
	}

	@Override
	public void beautify() {		
	}
}
