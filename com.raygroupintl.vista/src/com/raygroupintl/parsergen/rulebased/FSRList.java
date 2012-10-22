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

import com.raygroupintl.parser.TFList;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.AdapterSpecification;
import com.raygroupintl.parsergen.ruledef.RuleSupplyFlag;

public class FSRList<T extends Token> extends FSRBase<T> {
	private FactorySupplyRule<T> element;
	private TFList<T> factory;
	
	public FSRList(String name, RuleSupplyFlag flag, FactorySupplyRule<T> element) {
		super(flag);
		this.element = element;
		this.factory = new TFList<T>(name);
	}
	
	@Override
	public String getName() {
		return this.factory.getName();
	}
	
	@Override
	public boolean update(RulesByName<T> symbols) {
		RulesByNameLocal<T> localSymbols = new RulesByNameLocal<T>(symbols, this);
		this.element.update(localSymbols);
		TokenFactory<T> element = this.element.getTheFactory(localSymbols);
		this.factory.setElement(element);
		return true;
	}

	@Override
	public TFList<T> getShellFactory() {
		return this.factory;
	}

	@Override
	public void setAdapter(AdapterSpecification<T> spec) {
		 Constructor<? extends T> a = spec.getListTokenAdapter();
		 if (a != null) this.factory.setListTargetType(a);
	}
}