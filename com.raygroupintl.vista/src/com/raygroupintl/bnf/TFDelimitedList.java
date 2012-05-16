package com.raygroupintl.bnf;

import java.util.Arrays;
import java.util.List;

public class TFDelimitedList extends TokenFactory {
	private TFSequenceStatic effective;	
	private ListAdapter adapter;
	
	public TFDelimitedList(String name) {
		this(name, new DefaultListAdapter());
	}
	
	public TFDelimitedList(String name, ListAdapter adapter) {
		super(name);
		this.adapter = adapter;
	}
		
	protected Token getToken(List<Token> tokens) {
		if (this.adapter == null) {
			return new TDelimitedList(tokens);
		} else {
			return this.adapter.convert(tokens);
		}
	}
	
	private TokenFactory getLeadingFactory(TokenFactory element, TokenFactory delimiter, boolean emptyAllowed) {
		if (emptyAllowed) {
			String elementName = this.getName() + "." + element.getName();
			String emptyName = this.getName() + "." + "empty";
			return new TFChoiceBasic(elementName, element, new TFEmpty(emptyName, delimiter));	
		} else {
			return element;
		}
	}
	
	public void set(TokenFactory element, TokenFactory delimiter, boolean emptyAllowed) {
		TokenFactory leadingElement = this.getLeadingFactory(element, delimiter, emptyAllowed);
		String tailElementName = this.getName() + "." + "tailelement";
		TFSequenceStatic tailElement = new TFSequenceStatic(tailElementName, delimiter, element);
		tailElement.setRequiredFlags(true, !emptyAllowed);
		String tailListName = this.getName() + "." + "taillist";
		TokenFactory tail = new TFList(tailListName, tailElement);
		String name = this.getName() + "." + "effective";
		this.effective = new TFSequenceStatic(name, leadingElement, tail);
		this.effective.setRequiredFlags(true, false);
	}
	
	public void set(TokenFactory element, TokenFactory delimiter) {
		this.set(element, delimiter, false);
	}

	@Override
	public Token tokenize(Text text) throws SyntaxErrorException {
		if (this.effective == null) {
			throw new IllegalStateException("TFDelimitedList.set needs to be called before TFDelimitedList.tokenize");
		} else {
			TSequence internalResult = (TSequence) this.effective.tokenize(text);
			if (internalResult == null) {
				return null;
			} else {
				Token leadingToken = internalResult.get(0);
				Token tailTokens = internalResult.get(1);
				if (tailTokens == null) {
					Token[] tmpResult = {leadingToken};
					return this.getToken(Arrays.asList(tmpResult));	
				} else {		
					TList result = (TList) tailTokens;
					List<Token> list = result.getList();
					list.add(0, leadingToken);
					return this.getToken(list);
				}
			}
		}
	}
}
