package com.raygroupintl.bnf;

import java.util.List;

public final class TFList extends TokenFactory {
	private TokenFactory elementFactory;
	private boolean addErrorToList;
	
	public TFList() {
	}
	
	public TFList(TokenFactory elementFactory) {
		this.elementFactory = elementFactory;
	}
	
	public void setElementFactory(TokenFactory elementFactory) {
		this.elementFactory = elementFactory;
	}
		
	public void setAddErrorToList(boolean b) {
		this.addErrorToList = b;
	}
	
	protected Token getToken(List<Token> list) {
		return new TList(list);
	}

	@Override
	public Token tokenize(String line, int fromIndex) throws SyntaxErrorInListException {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			int index = fromIndex;
			ListAsTokenStore list = new ListAsTokenStore();
			while (index < endIndex) {
				try {
					Token token = this.elementFactory.tokenize(line, index);
					if (token == null) {
						if (list.hasToken()) {
							return this.getToken(list.toList());
						} else {
							return null;
						}
					}
					list.addToken(token);	
					index += token.getStringSize();
				} catch (SyntaxErrorException se) {
					if (this.addErrorToList) {
						Token et = se.getAsToken(line, index);
						list.addToken(et);
						return this.getToken(list.toList());
					} else {
						throw new SyntaxErrorInListException(se, list);
					}
				}
			}
			assert(index == endIndex);	
			return this.getToken(list.toList());
		}
		return null;
	}
}
