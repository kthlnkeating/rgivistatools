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

import com.raygroupintl.parser.TFCopy;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.AdapterSpecification;
import com.raygroupintl.parsergen.ruledef.RuleSupplyFlag;

public class FSRCopy extends FSRBase {
	private String masterName;
	private TFCopy factory;
	
	public FSRCopy(String name, String masterName) {
		super(RuleSupplyFlag.TOP);
		this.masterName = masterName;
		this.factory = new TFCopy(name);
	}
	
	@Override
	public String getName() {
		return this.factory.getName();
	}
	
	@Override
	public FactorySupplyRule getLeading(RulesByName names, int level) {
		return names.get(this.masterName);
	}
	
	@Override
	public TokenFactory getShellFactory() {
		return this.factory;
	}
	
	@Override
	public boolean update(RulesByName symbols) {
		RulesByNameLocal localSymbols = new RulesByNameLocal(symbols, this);
		FactorySupplyRule fsrMaster = symbols.get(this.masterName);
		if ((fsrMaster != null) && fsrMaster.update(localSymbols)) {
			TokenFactory f = fsrMaster.getTheFactory(localSymbols);
			this.factory.setMaster(f);
			return true;
		}
		return false;
	}

	@Override
	public void setAdapter(AdapterSpecification spec) {
		 Class<? extends Token> a = spec.getTokenAdapter();
		 if (a != null) this.factory.setTargetType(a);
	}
	
	@Override
	public String[] getNeededNames() {
		return new String[]{this.masterName};
	}
}