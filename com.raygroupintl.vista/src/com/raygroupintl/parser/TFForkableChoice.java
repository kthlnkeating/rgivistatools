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

import com.raygroupintl.parser.annotation.AdapterSupply;

public class TFForkableChoice extends TFBasic {
	private List<TokenFactory> factories = new ArrayList<TokenFactory>();
	
	public TFForkableChoice(String name) {
		super(name);
	}
	
	public void reset() {
		this.factories = new ArrayList<TokenFactory>();
	}
	
	public void setFactories(List<TokenFactory> factories) {
		this.factories = factories;
	}
	
	@Override
	public Token tokenize(Text text, AdapterSupply adapterSupply) throws SyntaxErrorException {
		if (text.onChar()) {
			for (TokenFactory f : this.factories) {
				Token result = f.tokenize(text, adapterSupply);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}
	
	public void setTargetType(Class<? extends Token> cls) {
		throw new UnsupportedOperationException("Target type is not supported for choice tokens");		
	}	
}
