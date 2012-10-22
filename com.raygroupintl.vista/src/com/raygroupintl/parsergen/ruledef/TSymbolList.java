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

import com.raygroupintl.parser.SequenceOfTokens;
import com.raygroupintl.parser.Token;

public class TSymbolList extends TSequence implements SymbolList {
	public TSymbolList(int length) {
		super(length);
	}
	
	public TSymbolList(SequenceOfTokens<RuleSupply> tokens) {
		super(tokens);
	}
	
	@Override
	public RuleSupply getElement() {
		return (RuleSupply) this.getToken(1);
	}
	
	@Override
	public RuleSupply getDelimiter() {
		return (RuleSupply) this.getToken(2, 1);
	}
	
	@Override
	public RuleSupply getLeftParanthesis() {
		return (RuleSupply) this.getToken(2, 2, 1);
	}
	
	@Override
	public RuleSupply getRightParanthesis() {
		return (RuleSupply)  this.getToken(2, 2, 3);
	}
	
	private boolean getFlag(int index) {
		Token f = this.getToken(2, 2, index);
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
