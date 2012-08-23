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

import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;

public class TSymbolList extends TSequence implements RuleSupply {
	public TSymbolList(Token token) {
		super(token);
	}
	
	private ListInfo getListInfo(String name, Map<String, RuleSupply> existing) {
		ListInfo result = new ListInfo();
		TSequence listInfoSpec = (TSequence) this.get(2);
		if (listInfoSpec == null) {
			return result;
		}
		RuleSupply delimiter = (RuleSupply) listInfoSpec.get(1);
		result.delimiter = delimiter.getRule(RuleSupplyFlag.INNER_REQUIRED, name + ".delimiter", existing);
		if (result.delimiter == null) {
			return null;
		}
		TSequence otherSpec = (TSequence)listInfoSpec.get(2);
		if (otherSpec != null) {
			RuleSupply leftSpec = (RuleSupply) otherSpec.get(1);
			result.left = leftSpec.getRule(RuleSupplyFlag.INNER_REQUIRED, name + ".left", existing);
			RuleSupply rightSpec = (RuleSupply) otherSpec.get(3);
			result.right = rightSpec.getRule(RuleSupplyFlag.INNER_REQUIRED, name + ".right", existing);
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
	public FactorySupplyRule getRule(RuleSupplyFlag flag, String name, Map<String, RuleSupply> existing) {
		RuleSupplyFlag innerFlag = flag.demoteInner();
		RuleSupply ers = (RuleSupply) this.get(1);
		FactorySupplyRule e = ers.getRule(innerFlag, name + ".element", existing);
		if (e == null) {
			return null;
		}		
		ListInfo listInfo = this.getListInfo(name, existing);
		if (listInfo == null) {
			return null;
		}
		return e.formList(name, innerFlag, listInfo);
	}
}
