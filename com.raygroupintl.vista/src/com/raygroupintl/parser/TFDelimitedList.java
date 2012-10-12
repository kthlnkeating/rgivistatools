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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.parsergen.ObjectSupply;

public class TFDelimitedList extends TokenFactory {
	private TFSequence effective;	
	private Constructor<? extends CompositeToken> constructor;
	
	public TFDelimitedList(String name) {
		super(name);
	}
		
	private TokenFactory getLeadingFactory(TokenFactory element, TokenFactory delimiter, boolean emptyAllowed) {
		if (emptyAllowed) {
			String elementName = this.getName() + "." + element.getName();
			String emptyName = this.getName() + "." + "empty";
			return new TFChoice(elementName, element, new TFEmpty(emptyName, delimiter));	
		} else {
			return element;
		}
	}
	
	public void set(TokenFactory element, TokenFactory delimiter, boolean emptyAllowed) {
		TokenFactory leadingElement = this.getLeadingFactory(element, delimiter, emptyAllowed);
		String tailElementName = this.getName() + "." + "tailelement";
		TFSequence tailElement = new TFSequence(tailElementName, delimiter, emptyAllowed ? leadingElement : element);
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

	private CompositeToken convertList(ObjectSupply objectSupply, List<Token> list) {
		if (this.constructor == null) {
			return objectSupply.newDelimitedList(list);			
		} else {
			try {
				return this.constructor.newInstance(list);						
			} catch (Throwable t) {
				String clsName =  this.getClass().getName();
				Logger.getLogger(clsName).log(Level.SEVERE, "Unable to instantiate " + clsName + ".", t);			
			}
			return null;			
		}
	}
	
	@Override
	protected CompositeToken tokenizeOnly(Text text, ObjectSupply objectSupply) throws SyntaxErrorException {
		if (this.effective == null) {
			throw new IllegalStateException("TFDelimitedList.set needs to be called before TFDelimitedList.tokenize");
		} else {
			CompositeToken internalResult = this.effective.tokenizeOnly(text, objectSupply);
			if (internalResult == null) {
				return null;
			} else {
				Token leadingToken = internalResult.get(0);
				Token tailTokens = internalResult.get(1);
				if (tailTokens == null) {
					Token[] tmpResult = {leadingToken};
					List<Token> list = Arrays.asList(tmpResult);
					return this.convertList(objectSupply, list);
				} else {		
					List<Token> list = ((TokenStore) tailTokens).toList();
					list.add(0, leadingToken);
					int lastIndex = list.size() - 1;
					List<Token> lastToken = ((TokenStore)list.get(lastIndex)).toList();
					if ((lastToken.size() < 2) || (lastToken.get(1) == null)) {
						TSequence newLast = objectSupply.newSequence(2);
						newLast.addToken(lastToken.get(0));
						newLast.addToken(objectSupply.newEmpty());
						list.set(lastIndex, newLast);
					}
					return this.convertList(objectSupply, list);
				}
			}
		}
	}
	
	public void setDelimitedListTargetType(Class<? extends CompositeToken> cls) {
		this.constructor = this.getConstructor(cls, List.class);
	}
}
