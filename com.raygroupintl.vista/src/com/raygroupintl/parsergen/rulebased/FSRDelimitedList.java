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

import com.raygroupintl.parser.TFDelimitedList;
import com.raygroupintl.parsergen.ruledef.RuleSupplyFlag;

public class FSRDelimitedList extends FSRBase {
	private FactorySupplyRule element;
	private FactorySupplyRule delimiter;
	private TFDelimitedList factory;
	
	public FSRDelimitedList(String name, RuleSupplyFlag flag, FactorySupplyRule element, FactorySupplyRule delimiter) {
		super(flag);
		this.element = element;
		this.delimiter = delimiter;
		this.factory = new TFDelimitedList(name);
	}
	
	@Override
	public String getName() {
		return this.factory.getName();
	}
	
	@Override
	public boolean update(RulesByName symbols) {
		RulesByNameLocal localSymbols = new RulesByNameLocal(symbols, this);
		this.element.update(localSymbols);
		this.delimiter.update(localSymbols);

		this.factory.set(this.element.getTheFactory(symbols), this.delimiter.getTheFactory(symbols), false);				
		return true;
	}

	@Override
	public TFDelimitedList getShellFactory() {
		return this.factory;
	}
}