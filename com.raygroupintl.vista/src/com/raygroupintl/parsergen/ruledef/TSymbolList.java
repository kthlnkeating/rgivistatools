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

import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenStore;

public class TSymbolList extends TSequence implements SymbolList {
	public TSymbolList(int length) {
		super(length);
	}
	
	public TSymbolList(TokenStore store) {
		super(store.toList());
	}
	
	@Override
	public RuleSupply getElement() {
		return (RuleSupply) this.get(1);
	}
	
	private static TokenStore getGrandChild(TokenStore parent, int index0, int index1) {
		TokenStore gc = (TokenStore) parent.get(index0);
		if (gc == null) {
			return null;
		} else {
			return (TokenStore) gc.get(index1);
		}		
	}
	
	@Override
	public RuleSupply getDelimiter() {
		return (RuleSupply) getGrandChild(this, 2, 1);
	}
	
	private static Token getGreatGrandChild(TokenStore parent, int index0, int index1, int index2) {
		TokenStore gc = getGrandChild(parent, index0, index1);
		if (gc == null) {
			return null;
		} else {
			return gc.get(index2);
		}		
	}
	
	@Override
	public RuleSupply getLeftParanthesis() {
		return (RuleSupply) getGreatGrandChild(this, 2, 2, 1);
	}
	
	@Override
	public RuleSupply getRightParanthesis() {
		return (RuleSupply) getGreatGrandChild(this, 2, 2, 3);
	}
	
	private boolean getFlag(int index) {
		Token f = getGreatGrandChild(this, 2, 2, index);
		if (f == null) {
			return false;
		} else {
			return f.toValue().toString().equals("1");
		}		
	}
	
	@Override
	public boolean isEmptyAllowed() {
		return this.getFlag(5);
	}

	@Override
	public boolean isNoneAllowed() {
		return this.getFlag(7);
	}

	@Override
	public void accept(RuleDefinitionVisitor visitor, String name, RuleSupplyFlag flag) {
		visitor.visitSymbolList(this, name, flag);
	}
}
