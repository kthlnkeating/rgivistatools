package com.raygroupintl.parser;

import com.raygroupintl.parsergen.ObjectSupply;

public class TFCopy extends TokenFactory {
	private TokenFactory tf;
	
	public TFCopy(String name) {
		super(name);
	}
	
	public void setMaster(TokenFactory master) {
		this.tf = master;
	}
	
	@Override
	protected Token tokenizeOnly(Text text, ObjectSupply objectSupply) throws SyntaxErrorException {
		return this.tf.tokenize(text, objectSupply);
	}
}
