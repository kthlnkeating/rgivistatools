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

public final class TFList extends TokenFactory {
	private TokenFactory elementFactory;
	private Constructor<? extends Token> constructor;
	
	public TFList(String name) {
		super(name);
	}
	
	public TFList(String name, TokenFactory elementFactory) {
		super(name);
		this.elementFactory = elementFactory;
	}
	
	public void setElement(TokenFactory elementFactory) {
		this.elementFactory = elementFactory;
	}

	public ListOfTokens tokenizeCommon(Text text, ObjectSupply objectSupply) throws SyntaxErrorException {
		if (text.onChar()) {
			ListOfTokens list = new ListOfTokens();
			while (text.onChar()) {
				Token token = null;
				try {
					token = this.elementFactory.tokenize(text, objectSupply);
				} catch (SyntaxErrorException e) {
					throw e;
				}
				if (token == null) {
					if (list.hasToken()) {
						return list;
					} else {
						return null;
					}
				}
				list.addToken(token);	
			}
			return list;
		}
		return null;
	}
	
	@Override
	public Token tokenizeOnly(Text text, ObjectSupply objectSupply) throws SyntaxErrorException {
		if (elementFactory == null) throw new IllegalStateException("TFList.setElementFactory needs to be called before TFList.tokenize");
		ListOfTokens tokens = this.tokenizeCommon(text, objectSupply);
		return this.convertList(tokens, objectSupply);
	}
	
	public Token convertList(ListOfTokens tokens, ObjectSupply objectSupply) {
		if (tokens == null) return null;
		if (this.constructor == null) {
			return objectSupply.newList(tokens);
		} else {
			try {
				return this.constructor.newInstance(tokens);						
			} catch (Throwable t) {
				String clsName =  this.getClass().getName();
				Logger.getLogger(clsName).log(Level.SEVERE, "Unable to instantiate " + clsName + ".", t);			
			}
			return null;
		}
	}

	public void setListTargetType(Class<? extends Token> cls, Class<ListOfTokens> listCls) {
		this.constructor = this.getConstructor(cls, listCls);		
	}
}
