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

import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.ParseErrorException;
import com.raygroupintl.parsergen.ruledef.RuleSupplyFlag;

public class FSRSingle extends FSRBase {
	private String value;
		
	public FSRSingle(String value, RuleSupplyFlag flag) {
		super(flag);
		this.value = value;
	}
	
	@Override
	public String getName() {
		return this.value;
	}
	
	@Override
	public FactorySupplyRule getLeading(RulesByName names) {
		return names.get(this.value);
	}	
	
	@Override
	public FactorySupplyRule getActualRule(RulesByName symbols) {
		FactorySupplyRule f = symbols.get(this.value);
		if (f == null) {
			throw new ParseErrorException("Undefined symbol " + value + " used in the rule");
		}
		return f;
	}
	
	@Override
	public TokenFactory getShellFactory() {
		throw new ParseErrorException("Not a top rule.");
	}
}