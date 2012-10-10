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

package com.raygroupintl.parsergen.ruledef;

import java.util.List;
import java.util.Map;

import com.raygroupintl.parser.TDelimitedList;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parsergen.rulebased.FSRChoice;
import com.raygroupintl.parsergen.rulebased.FactorySupplyRule;

public class TChoice extends TDelimitedList implements RuleSupply {
	public TChoice(List<Token> tokens) {
		super(tokens);
	}
	
	@Override
	public FactorySupplyRule getRule(RuleSupplyFlag flag, String name, Map<String, RuleSupply> existing) {
		if (this.size() == 1) {
			RuleSupply r = (RuleSupply) this.get(0);
			return r.getRule(flag, name, existing);
		} else {
			int index = 0;
			FSRChoice result = new FSRChoice(name, flag);
			for (Token t : this) {
				RuleSupply r  = (RuleSupply) t;
				FactorySupplyRule fsr = r.getRule(flag.demoteInner(), name + "." + String.valueOf(index), existing);
				if (fsr == null) return null;
				result.add(fsr);
				++index;
			}
			return result;
		}
	}
}