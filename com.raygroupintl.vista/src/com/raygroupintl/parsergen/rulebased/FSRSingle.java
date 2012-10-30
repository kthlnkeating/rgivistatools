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

package com.raygroupintl.parsergen.rulebased;

import com.raygroupintl.parser.Adapter;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.ParseErrorException;

public class FSRSingle<T extends Token> extends FSRBase<T> {
	private String name;
	private String value;
		
	public FSRSingle(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public FactorySupplyRule<T> getLeading(RulesByName<T> names, int level) {
		return names.get(this.value).getLeading(names, level);
	}	
	
	@Override
	public FactorySupplyRule<T> getActualRule(RulesByName<T> symbols) {
		FactorySupplyRule<T> f = symbols.get(this.value);
		if (f == null) {
			throw new ParseErrorException("Undefined symbol " + value + " used in the rule");
		}
		return f;
	}
	
	@Override
	public TokenFactory<T> getShellFactory() {
		throw new ParseErrorException("Not a top rule.");
	}
	
	@Override
	public Adapter<T> getAdapter(RulesByName<T> names) {
		return names.get(this.value).getAdapter(names);
	}
}