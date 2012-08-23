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

package com.raygroupintl.parser.annotation;

class RulesByNameLocal implements RulesByName {
	private RulesByName factories;
	private FactorySupplyRule me;
	
	public RulesByNameLocal(RulesByName factories, FactorySupplyRule me) {
		this.factories = factories;
		this.me = me;
	}
	
	@Override
	public FactorySupplyRule get(String name) {
		if (this.me.getName().equals(name)) {
			return this.me;
		} else {
			return this.factories.get(name);
		}
	}
	
	@Override
	public void put(String name, FactorySupplyRule r) {
		this.factories.put(name, r);
	}
	
	@Override
	public boolean hasRule(String name) {
		if (this.me.getName().equals(name)) {
			return true;
		} else {
			return this.factories.get(name) != null;
		}
	}
}
