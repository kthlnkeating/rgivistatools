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

import com.raygroupintl.parser.TDelimitedList;
import com.raygroupintl.parser.Token;

public class TSymbolSequence extends TDelimitedList implements RuleSupply {
	public TSymbolSequence(List<Token> tokens) {
		super(tokens);
	}
	
	@Override
	public void accept(RuleDefinitionVisitor visitor, String name, RuleSupplyFlag flag) {
		if (this.size() == 1) {
			((RuleSupply) this.get(0)).accept(visitor, name, flag);	
		} else {
			visitor.visitSymbolSequence(this, name, flag);			
		}
	}
}