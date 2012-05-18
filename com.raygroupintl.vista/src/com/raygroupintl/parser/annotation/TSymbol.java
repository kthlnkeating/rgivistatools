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

import com.raygroupintl.parser.TString;
import com.raygroupintl.parser.TokenFactory;

public class TSymbol extends TString implements RulePieceGenerator {
	public TSymbol(String value) {
		super(value);
	}
	
	@Override
	public TokenFactory getFactory(String name, Map<String, TokenFactory> symbols) {
		String value = this.getStringValue();
		return symbols.get(value);
	}
	
	@Override	
	public boolean getRequired() {
		return true;
	}	

	@Override
	public TokenFactory getPreliminaryTop(String name) {
		return null;
	}
}
