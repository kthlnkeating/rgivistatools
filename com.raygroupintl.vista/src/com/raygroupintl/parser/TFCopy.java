package com.raygroupintl.parser;

import com.raygroupintl.parsergen.ObjectSupply;

public class TFCopy<T extends Token> extends TFWithAdaptor<T> {
	private TokenFactory<T> tf;
	
	public TFCopy(String name) {
		super(name);
	}
	
	public void setMaster(TokenFactory<T> master) {
		this.tf = master;
	}
	
	@Override
	protected T tokenizeOnly(Text text, ObjectSupply<T> objectSupply) throws SyntaxErrorException {
		return this.tf.tokenize(text, objectSupply);
	}
}
