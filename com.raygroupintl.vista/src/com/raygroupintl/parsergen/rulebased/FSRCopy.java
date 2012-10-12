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

import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.AdapterSpecification;
import com.raygroupintl.parsergen.ruledef.RuleSupplyFlag;

public class FSRCopy extends FSRBase {
	private FactorySupplyRule slave;
	
	public FSRCopy(FactorySupplyRule slave) {
		super(RuleSupplyFlag.TOP);
		this.slave = slave;
	}
	
	@Override
	public String getName() {
		return this.slave.getName();
	}
	
	@Override
	public FactorySupplyRule getLeading(RulesByName names) {
		return this.slave;
	}
	
	@Override
	public TokenFactory getShellFactory() {
		return this.slave.getShellFactory();
	}
	
	@Override
	public int getSequenceCount() {
		return this.slave.getSequenceCount();
	}

	@Override
	public boolean update(RulesByName symbols) {
		return this.slave.update(symbols);
	}

	@Override
	public void setAdapter(AdapterSpecification spec) {
		this.slave.setAdapter(spec);
	}
}