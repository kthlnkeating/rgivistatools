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

import java.lang.reflect.Constructor;

import com.raygroupintl.parser.TFDelimitedList;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parsergen.AdapterSpecification;
import com.raygroupintl.parsergen.ruledef.RuleSupplyFlag;

public class FSRDelimitedList<T extends Token> extends FSRBase<T> {
	private FactorySupplyRule<T> element;
	private FactorySupplyRule<T> delimiter;
	private TFDelimitedList<T> factory;
	
	public FSRDelimitedList(String name, RuleSupplyFlag flag, FactorySupplyRule<T> element, FactorySupplyRule<T> delimiter) {
		super(flag);
		this.element = element;
		this.delimiter = delimiter;
		this.factory = new TFDelimitedList<T>(name);
	}
	
	@Override
	public String getName() {
		return this.factory.getName();
	}
	
	@Override
	public boolean update(RulesByName<T> symbols) {
		RulesByNameLocal<T> localSymbols = new RulesByNameLocal<T>(symbols, this);
		this.element.update(localSymbols);
		this.delimiter.update(localSymbols);

		this.factory.set(this.element.getTheFactory(symbols), this.delimiter.getTheFactory(symbols), false);				
		return true;
	}

	@Override
	public TFDelimitedList<T> getShellFactory() {
		return this.factory;
	}
	
	@Override
	public void setAdapter(AdapterSpecification<T> spec) {
		 Constructor<? extends T> a = spec.getDelimitedListTokenAdapter();
		 if (a != null) this.factory.setDelimitedListTargetType(a);
	}	
}