package com.raygroupintl.bnf;

import java.util.List;

public final class TFList extends TokenFactory {
	private TokenFactory elementFactory;
	
	public TFList() {
	}
	
	public TFList(TokenFactory elementFactory) {
		this.elementFactory = elementFactory;
	}
	
	public void setElementFactory(TokenFactory elementFactory) {
		this.elementFactory = elementFactory;
	}
		
	protected Token getToken(List<Token> list) {
		return new TList(list);
	}

	@Override
	public Token tokenize(String line, int fromIndex) throws SyntaxErrorException {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			int index = fromIndex;
			ListAsTokenStore list = new ListAsTokenStore();
			while (index < endIndex) {
				Token token = null;
				try {
					token = this.elementFactory.tokenize(line, index);
				} catch (SyntaxErrorException e) {
					e.addStore(list);
					throw e;
				}
				if (token == null) {
					if (list.hasToken()) {
						return this.getToken(list.toList());
					} else {
						return null;
					}
				}
				list.addToken(token);	
				index += token.getStringSize();
			}
			assert(index == endIndex);	
			return this.getToken(list.toList());
		}
		return null;
	}
}
