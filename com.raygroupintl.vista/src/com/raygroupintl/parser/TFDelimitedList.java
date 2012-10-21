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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.parsergen.ObjectSupply;

public class TFDelimitedList extends TokenFactory {
	private TFSequence effective;	
	private Constructor<? extends Token> constructor;
	
	public TFDelimitedList(String name) {
		super(name);
	}
		
	private TokenFactory getLeadingFactory(TokenFactory element, TokenFactory delimiter, boolean emptyAllowed) {
		if (emptyAllowed) {
			String elementName = this.getName() + "." + element.getName();
			String emptyName = this.getName() + "." + "empty";
			TFChoice result = new TFChoice(elementName);
			result.add(element);
			result.add(new TFEmpty(emptyName, delimiter));
			return result;
		} else {
			return element;
		}
	}
	
	public void set(TokenFactory element, TokenFactory delimiter, boolean emptyAllowed) {
		TokenFactory leadingElement = this.getLeadingFactory(element, delimiter, emptyAllowed);
		String tailElementName = this.getName() + "." + "tailelement";
		TFSequence tailElement = new TFSequence(tailElementName, 2);
		tailElement.add(delimiter, true);
		tailElement.add(emptyAllowed ? leadingElement : element, !emptyAllowed);
		String tailListName = this.getName() + "." + "taillist";
		TokenFactory tail = new TFList(tailListName, tailElement);
		String name = this.getName() + "." + "effective";
		this.effective = new TFSequence(name,2);
		this.effective.add(leadingElement, true);
		this.effective.add(tail, false);
	}
	
	public void set(TokenFactory element, TokenFactory delimiter) {
		this.set(element, delimiter, false);
	}

	private Token convertList(ObjectSupply objectSupply, Token leadingToken, Tokens tailTokens) {
		if (this.constructor == null) {
			return objectSupply.newDelimitedList(leadingToken, tailTokens);			
		} else {
			try {
				return this.constructor.newInstance(leadingToken, tailTokens);						
			} catch (Throwable t) {
				String clsName =  this.getClass().getName();
				Logger.getLogger(clsName).log(Level.SEVERE, "Unable to instantiate " + clsName + ".", t);			
			}
			return null;			
		}
	}
	
	@Override
	protected Token tokenizeOnly(Text text, ObjectSupply objectSupply) throws SyntaxErrorException {
		if (this.effective == null) {
			throw new IllegalStateException("TFDelimitedList.set needs to be called before TFDelimitedList.tokenize");
		} else {
			SequenceOfTokens internalResult = this.effective.tokenizeCommon(text, objectSupply);
			if (internalResult == null) {
				return null;
			} else {
				Token leadingToken = internalResult.getToken(0);
				Tokens tailTokens = internalResult.getTokens(1);
				if (tailTokens == null) {
					return this.convertList(objectSupply, leadingToken, null);
				} else {
					int lastIndex = tailTokens.size() - 1;
					Tokens lastToken = tailTokens.getTokens(lastIndex);
					if (lastToken.getToken(1) == null) {
						lastToken.setToken(1, objectSupply.newEmpty());
					}
					return this.convertList(objectSupply, leadingToken, tailTokens);
				}
			}
		}
	}
	
	public void setDelimitedListTargetType(Constructor<? extends Token> constructor) {
		this.constructor = constructor;
	}
}
