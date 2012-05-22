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

import java.util.List;
import java.util.Map;

import com.raygroupintl.parser.TDelimitedList;
import com.raygroupintl.parser.TFForkableChoice;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;

public class TChoice extends TDelimitedList implements TopTFRule, FactorySupplyRule {
	public TChoice(List<Token> tokens) {
		super(tokens);
	}
	
	@Override
	public TokenFactory getFactory(String name, Map<String, TokenFactory> symbols) {
		if (this.size() == 1) {
			FactorySupplyRule r = (FactorySupplyRule) this.get(0);
			return r.getFactory(name, symbols);
		} else {
			TFForkableChoice result = new TFForkableChoice(name);
			for (Token t : this) {
				FactorySupplyRule r = (FactorySupplyRule) t;
				TokenFactory f = r.getFactory(name, symbols);
				if (f == null) {
					return null;
				}
				result.add(f);
			}
			return result;
		}
	}
	
	@Override	
	public boolean getRequired() {
		return true;
	}	

	@Override
	public TokenFactory getTopFactory(String name, Map<String, TokenFactory> symbols, boolean asShell) {
		if (this.size() == 1) {
			TopTFRule r = (TopTFRule) this.get(0);
			return r.getTopFactory(name, symbols, asShell);
		} else {
			if (asShell) {
				return new TFForkableChoice(name);	
			} else {
				return this.getFactory(name, symbols);
			}
		}
	}
}