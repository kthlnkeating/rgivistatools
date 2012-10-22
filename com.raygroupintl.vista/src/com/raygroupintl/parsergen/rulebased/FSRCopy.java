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

import com.raygroupintl.parser.TFCopy;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.AdapterSpecification;
import com.raygroupintl.parsergen.ruledef.RuleSupplyFlag;

public class FSRCopy<T extends Token> extends FSRBase<T> {
	private String masterName;
	private TFCopy<T> factory;
	
	public FSRCopy(String name, String masterName) {
		super(RuleSupplyFlag.TOP);
		this.masterName = masterName;
		this.factory = new TFCopy<T>(name);
	}
	
	@Override
	public String getName() {
		return this.factory.getName();
	}
	
	@Override
	public FactorySupplyRule<T> getLeading(RulesByName<T> names, int level) {
		return names.get(this.masterName);
	}
	
	@Override
	public TokenFactory<T> getShellFactory() {
		return this.factory;
	}
	
	@Override
	public boolean update(RulesByName<T> symbols) {
		RulesByNameLocal<T> localSymbols = new RulesByNameLocal<T>(symbols, this);
		FactorySupplyRule<T> fsrMaster = symbols.get(this.masterName);
		if ((fsrMaster != null) && fsrMaster.update(localSymbols)) {
			TokenFactory<T> f = fsrMaster.getTheFactory(localSymbols);
			this.factory.setMaster(f);
			return true;
		}
		return false;
	}

	@Override
	public void setAdapter(AdapterSpecification<T> spec) {
		 Constructor<? extends T> constructor = spec.getTokenAdapter();
		 if (constructor != null) this.factory.setTargetType(constructor);
	}
	
	@Override
	public String[] getNeededNames() {
		return new String[]{this.masterName};
	}
}