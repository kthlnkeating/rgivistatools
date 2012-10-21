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

import com.raygroupintl.parser.ListOfTokens;
import com.raygroupintl.parser.TFList;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.AdapterSpecification;
import com.raygroupintl.parsergen.ruledef.RuleSupplyFlag;

public class FSRList extends FSRBase {
	private FactorySupplyRule element;
	private TFList factory;
	
	public FSRList(String name, RuleSupplyFlag flag, FactorySupplyRule element) {
		super(flag);
		this.element = element;
		this.factory = new TFList(name);
	}
	
	@Override
	public String getName() {
		return this.factory.getName();
	}
	
	@Override
	public boolean update(RulesByName symbols) {
		RulesByNameLocal localSymbols = new RulesByNameLocal(symbols, this);
		this.element.update(localSymbols);
		TokenFactory element = this.element.getTheFactory(localSymbols);
		this.factory.setElement(element);
		return true;
	}

	@Override
	public TFList getShellFactory() {
		return this.factory;
	}

	@Override
	public void setAdapter(AdapterSpecification<Token> spec) {
		 Class<? extends Token> a = spec.getListTokenAdapter();
		 if (a != null) this.factory.setListTargetType(a, ListOfTokens.class);
	}
}