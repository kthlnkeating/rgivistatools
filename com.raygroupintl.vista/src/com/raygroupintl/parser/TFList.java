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

import com.raygroupintl.parsergen.ObjectSupply;

public final class TFList extends TokenFactory {
	private TokenFactory elementFactory;
	
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
		
	@Override
	public CompositeToken tokenizeOnly(Text text, ObjectSupply objectSupply) throws SyntaxErrorException {
		if (elementFactory == null) throw new IllegalStateException("TFList.setElementFactory needs to be called before TFList.tokenize");
		
		if (text.onChar()) {
			CompositeToken list = objectSupply.newList();
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
}
