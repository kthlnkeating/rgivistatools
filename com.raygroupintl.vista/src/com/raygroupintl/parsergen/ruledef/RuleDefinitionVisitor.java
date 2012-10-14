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

public class RuleDefinitionVisitor {
	protected void visitCharSymbol(TCharSymbol charSymbol, String name, RuleSupplyFlag flag) {		
	}

	protected void visitConstSymbol(TConstSymbol constSymbol, String name, RuleSupplyFlag flag) {		
	}
	
	protected void visitSymbol(TSymbol symbol, String name, RuleSupplyFlag flag) {		
	}
	
	protected void visitSymbolList(TSymbolList symbolList, String name, RuleSupplyFlag flag) {		
	}
	
	protected void visitChoiceOfSymbolsElement(RuleSupply choiceOfSymbolsElement, String ruleName, RuleSupplyFlag flag, int index) {		
	}

	protected void visitChoiceOfSymbols(TChoiceOfSymbols choice, String name, RuleSupplyFlag flag) {		
	}
	
	protected void visitSymbolSequenceElement(RuleSupply sequenceElement, String sequenceName, int index) {		
	}
	
	protected void visitSymbolSequence(TSymbolSequence sequence, String name, RuleSupplyFlag flag) {
	}
}
