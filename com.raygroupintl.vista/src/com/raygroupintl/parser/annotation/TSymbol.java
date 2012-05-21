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
import com.raygroupintl.parser.TString;
import com.raygroupintl.parser.TokenFactory;

public class TSymbol extends TString implements RulePieceGenerator {
	public TSymbol(String value) {
		super(value);
	}
	
	@Override
	public TokenFactory getFactory(String name, Map<String, TokenFactory> symbols) {
		String value = this.getStringValue();
		TokenFactory result = symbols.get(value);
		if (result == null) throw new ParseErrorException("Undefined symbol " + value + " used in the rule");
		if (! result.isInitialized()) {
			return null;
		}
		return result;
	}
	
	@Override	
	public boolean getRequired() {
		return true;
	}	

	@Override
	public TFBasic getTopFactory(String name, Map<String, TokenFactory> symbols, boolean asShell) {
		String value = this.getStringValue();
		TokenFactory source = symbols.get(value);
		if (source == null) {
			if (! asShell) throw new ParseErrorException("Undefined symbol " + value + " used in the rule");
			return null;
		}
		if (source instanceof TFBasic) {
			return ((TFBasic) source).getCopy(name);
		} else {
			throw new ParseErrorException("Custom symbol " + value + " cannot be used as a top symbol in rules");
		}
	}
}
