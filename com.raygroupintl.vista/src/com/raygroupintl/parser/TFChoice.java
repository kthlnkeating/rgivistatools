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

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.parsergen.ObjectSupply;

public class TFChoice extends TokenFactory {
	private List<TokenFactory> factories;
	
	public TFChoice(String name) {
		super(name);
		this.factories = new ArrayList<TokenFactory>();
	}
	
	public TFChoice(String name, TokenFactory... factories) {
		super(name);
		this.factories = new ArrayList<TokenFactory>(factories.length);
		for (int i=0; i<factories.length; ++i) this.factories.add(factories[i]);
	}
	
	public void setFactories(TokenFactory... factories) {
		this.factories = new ArrayList<TokenFactory>(factories.length);
		for (int i=0; i<factories.length; ++i) this.factories.add(factories[i]);
	}
	
	public void reset(int length) {
		this.factories = new ArrayList<TokenFactory>(length);		
	}
	
	public void add(TokenFactory tf) {
		this.factories.add(tf);
	}
	
	@Override
	public Token tokenizeOnly(Text text, ObjectSupply objectSupply) throws SyntaxErrorException {
		if (text.onChar()) {
			for (TokenFactory f : this.factories) {
				Token result = f.tokenize(text, objectSupply);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}
}
