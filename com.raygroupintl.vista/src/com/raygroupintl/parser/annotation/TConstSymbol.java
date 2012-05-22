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

package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TFConstant;
import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;

public class TConstSymbol extends TSequence implements RulePieceGenerator {
	public TConstSymbol(java.util.List<Token> tokens) {
		super(tokens);
	}
	
	@Override
	public TokenFactory getFactory(String name, Map<String, TokenFactory> symbols) {
		String key = this.getStringValue();
		TokenFactory result = symbols.get(key);
		if (result == null) {		
			String value = key.substring(1, key.length()-1);
			result = new TFConstant(key, value);
			symbols.put(key, result);
		}
		return result;
	}
	
	@Override	
	public boolean getRequired() {
		return true;
	}
	
	@Override
	public TFBasic getTopFactory(String name, Map<String, TokenFactory> symbols, boolean asShell) {
		throw new ParseErrorException("Constant symbols cannot be rules.");
	}
}
