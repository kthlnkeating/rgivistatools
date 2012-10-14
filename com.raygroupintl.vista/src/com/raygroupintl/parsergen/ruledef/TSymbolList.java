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

import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenStore;
import com.raygroupintl.parsergen.ListInfo;
import com.raygroupintl.parsergen.rulebased.FactorySupplyRule;

public class TSymbolList extends TSequence implements RuleSupply {
	public TSymbolList(int length) {
		super(length);
	}
	
	public TSymbolList(TokenStore store) {
		super(store.toList());
	}
	
	private ListInfo getListInfo(String name) {
		ListInfo result = new ListInfo();
		TokenStore listInfoSpec = (TokenStore) this.get(2);
		if (listInfoSpec == null) {
			return result;
		}
		RuleSupply delimiter = (RuleSupply) listInfoSpec.get(1);
		result.delimiter = delimiter.getRule(RuleSupplyFlag.INNER_REQUIRED, name + ".delimiter");
		if (result.delimiter == null) {
			return null;
		}
		TokenStore otherSpec = (TokenStore)listInfoSpec.get(2);
		if (otherSpec != null) {
			RuleSupply leftSpec = (RuleSupply) otherSpec.get(1);
			result.left = leftSpec.getRule(RuleSupplyFlag.INNER_REQUIRED, name + ".left");
			RuleSupply rightSpec = (RuleSupply) otherSpec.get(3);
			result.right = rightSpec.getRule(RuleSupplyFlag.INNER_REQUIRED, name + ".right");
			Token emptyAllowedSpec = otherSpec.get(5);
			result.emptyAllowed = (emptyAllowedSpec != null) && (emptyAllowedSpec.toValue().toString().equals("1"));
			Token noneAllowedSpec = otherSpec.get(7);
			result.noneAllowed = (noneAllowedSpec != null) && (noneAllowedSpec.toValue().toString().equals("1"));
			if ((result.left == null) || (result.right == null)) {
				return null;
			}
		}
		return result;
	}
	
	@Override
	public FactorySupplyRule getRule(RuleSupplyFlag flag, String name) {
		RuleSupplyFlag innerFlag = flag.demoteInner();
		RuleSupply ers = (RuleSupply) this.get(1);
		FactorySupplyRule e = ers.getRule(innerFlag, name + ".element");
		if (e == null) {
			return null;
		}		
		ListInfo listInfo = this.getListInfo(name);
		if (listInfo == null) {
			return null;
		}
		return e.formList(name, innerFlag, listInfo);
	}
}
