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

import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.AdapterSpecification;
import com.raygroupintl.parsergen.ruledef.RuleSupplyFlag;

public interface FactorySupplyRule {
	FactorySupplyRule getActualRule(RulesByName symbols);
	boolean getRequired();
	TokenFactory getShellFactory();
	
	String getName();
	
	FactorySupplyRule formList(String name, RuleSupplyFlag flag, ListInfo info);
	int getSequenceCount();
	boolean update(RulesByName symbols);
	TokenFactory getTheFactory(RulesByName symbols);
	
	FactorySupplyRule getLeading(RulesByName names, int level);
	void setAdapter(AdapterSpecification<Token> spec);
	
	String[] getNeededNames();
}
