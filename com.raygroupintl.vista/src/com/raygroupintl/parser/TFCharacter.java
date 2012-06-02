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

import com.raygroupintl.charlib.Predicate;
import com.raygroupintl.parser.annotation.AdapterSupply;

public class TFCharacter extends TFBasic {
	private Predicate predicate;
	private StringAdapter adapter;
	
	public TFCharacter(String name, Predicate predicate) {
		super(name);
		this.predicate = predicate;
	}
	
	@Override
	public Token tokenize(Text text, AdapterSupply adapterSupply) {
		return text.extractChar(this.predicate, this.adapter == null ? adapterSupply.getStringAdapter() : this.adapter);
	}
	
	@Override
	public Token tokenizeRaw(Text text, AdapterSupply adapterSupply) {
		return text.extractChar(this.predicate, adapterSupply.getStringAdapter());
	}
	
	@Override
	protected Token convert(Token token) {
		if ((this.adapter != null) && (token instanceof TString)) {
			return this.adapter.convert(token.toValue()); 
		} else {
			return token;
		}
	}

	@Override
	public void setTargetType(Class<? extends Token> cls) {
		final Constructor<? extends Token> constructor = getConstructor(cls, StringPiece.class, TString.class);
		this.adapter = new StringAdapter() {			
			@Override
			public Token convert(StringPiece ch) {
				try{
					return (Token) constructor.newInstance(ch);
				} catch (Exception e) {	
					return null;
				}
			}
		};
	}
}
