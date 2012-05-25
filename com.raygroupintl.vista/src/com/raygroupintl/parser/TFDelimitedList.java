//---------------------------------------------------------------------------
// Copyright 2012 Ray Group International
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//---------------------------------------------------------------------------

package com.raygroupintl.parser;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

import com.raygroupintl.parser.annotation.AdapterSupply;

public class TFDelimitedList extends TFBasic {
	private TFSequence effective;	
	private DelimitedListAdapter adapter;
	
	public TFDelimitedList(String name) {
		super(name);
	}
		
	private TFDelimitedList(String name, TFSequence effective) {
		super(name);
		this.effective = effective;
	}
		
	@Override
	public void copyWoutAdapterFrom(TFBasic rhs) {
		if (rhs instanceof TFDelimitedList) {
			TFDelimitedList rhsCasted = (TFDelimitedList) rhs;
			this.effective = rhsCasted.effective;
		} else {
			throw new IllegalArgumentException("Illegal attemp to copy from " + rhs.getClass().getName() + " to " + TFDelimitedList.class.getName());
		}
	}
	
	@Override
	public TFBasic getCopy(String name) {
		return new TFDelimitedList(name, this.effective);
	}

	@Override
	public boolean isInitialized() {
		return this.effective != null;
	}

	private Token getToken(List<Token> tokens, AdapterSupply adapterSupply) {
		return this.adapter == null ? adapterSupply.getDelimitedListAdapter().convert(tokens) : this.adapter.convert(tokens);
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
		TFSequence tailElement = new TFSequence(tailElementName, delimiter, element);
		tailElement.setRequiredFlags(true, !emptyAllowed);
		String tailListName = this.getName() + "." + "taillist";
		TokenFactory tail = new TFList(tailListName, tailElement);
		String name = this.getName() + "." + "effective";
		this.effective = new TFSequence(name, leadingElement, tail);
		this.effective.setRequiredFlags(true, false);
	}
	
	public void set(TokenFactory element, TokenFactory delimiter) {
		this.set(element, delimiter, false);
	}

	private List<Token> tokenizeCommon(Text text, AdapterSupply adapterSupply) throws SyntaxErrorException {
		if (this.effective == null) {
			throw new IllegalStateException("TFDelimitedList.set needs to be called before TFDelimitedList.tokenize");
		} else {
			TSequence internalResult = (TSequence) this.effective.tokenize(text, adapterSupply);
			if (internalResult == null) {
				return null;
			} else {
				Token leadingToken = internalResult.get(0);
				Token tailTokens = internalResult.get(1);
				if (tailTokens == null) {
					Token[] tmpResult = {leadingToken};
					return Arrays.asList(tmpResult);	
				} else {		
					TList result = (TList) tailTokens;
					List<Token> list = result.getList();
					list.add(0, leadingToken);
					return list;
				}
			}
		}
	}
	
	@Override
	public Token tokenize(Text text, AdapterSupply adapterSupply) throws SyntaxErrorException {
		List<Token> rawResult = this.tokenizeCommon(text, adapterSupply);
		if (rawResult == null) {
			return null;
		} else {
			return this.getToken(rawResult, adapterSupply);	
		}
	}

	@Override
	public Token tokenizeRaw(Text text, AdapterSupply adapterSupply) throws SyntaxErrorException {
		List<Token> rawResult = this.tokenizeCommon(text, adapterSupply);
		if (rawResult == null) {
			return null;
		} else {
			return adapterSupply.getDelimitedListAdapter().convert(rawResult);	
		}
	}
	
	@Override
	protected Token convert(Token token) {
		if ((this.adapter != null) && (token instanceof TDelimitedList)) {
			return this.adapter.convert(((TDelimitedList) token).getList()); 
		} else {
			return token;
		}
	}

	@Override
	public void setTargetType(Class<? extends Token> cls) {
		final Constructor<? extends Token> constructor = getConstructor(cls, List.class, TDelimitedList.class);
		this.adapter = new DelimitedListAdapter() {			
			@Override
			public Token convert(List<Token> tokens) {
				try{
					return (Token) constructor.newInstance(tokens);
				} catch (Exception e) {	
					return null;
				}
			}
		};
	}
}
