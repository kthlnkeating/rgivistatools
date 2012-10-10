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

import java.util.Map;

import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenStore;
import com.raygroupintl.parsergen.rulebased.FSRSequence;
import com.raygroupintl.parsergen.rulebased.FactorySupplyRule;

public abstract class TSymbols extends TSequence implements RuleSupply {
	private boolean required;
	
	public TSymbols(int length, boolean required) {
		super(length);
		this.required = required;
	}

	public TSymbols(TokenStore store, boolean required) {
		super(store.toList());
		this.required = required;
	}

	private RuleSupplyFlag convert(RuleSupplyFlag flag) {
		switch (flag) {
			case INNER_OPTIONAL:
			case INNER_REQUIRED:
				return this.required ? RuleSupplyFlag.INNER_REQUIRED : RuleSupplyFlag.INNER_OPTIONAL;
			default:
				return flag;
		}
	}
	
	@Override
	public FactorySupplyRule getRule(RuleSupplyFlag flag, String name, Map<String, RuleSupply> existing) {
		TokenStore list = (TokenStore) this.get(1);
		RuleSupplyFlag newFlag = this.convert(flag);
		if (list.size() == 1) {
			return ((RuleSupply) list.get(0)).getRule(newFlag, name, existing);
		} else {		
			FSRSequence result = new FSRSequence(name, newFlag);
			int index = 0;
			for (Token t : list) {
				RuleSupply rs = (RuleSupply) t;
				FactorySupplyRule fsr = rs.getRule(RuleSupplyFlag.INNER_REQUIRED, name + "." + String.valueOf(index), existing);
				if (fsr == null) return null;
				result.add(fsr);
				++index;
			}
			return result;
		}
	}
}
