//---------------------------------------------------------------------------
// Copyright 2013 PwC
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

package com.pwc.us.rgi.parsergen.ruledef;

import com.pwc.us.rgi.parser.DelimitedListOfTokens;

public class TSymbolSequence extends TDelimitedList implements RuleSupplies {
	public TSymbolSequence(DelimitedListOfTokens<RuleSupply> tokens) {		
		super(tokens);
	}
	
	@Override
	public void accept(RuleDefinitionVisitor visitor, String name, RuleSupplyFlag flag) {
		if (this.size() == 1) {
			this.getToken(0).accept(visitor, name, flag);	
		} else {
			visitor.visitSymbolSequence(this, name, flag);			
		}
	}

	@Override
	public int getSize() {
		return this.size();
	}
	
	@Override
	public void acceptElement(RuleDefinitionVisitor visitor, int index, String name, RuleSupplyFlag flag) {
		RuleSupply rs = this.getLogicalToken(index);
		rs.accept(visitor, name, flag);
	}
}
