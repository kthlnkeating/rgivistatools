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

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.ruledef.RuleSupplyFlag;

public class FSRStringSequence<T extends Token> extends FSRBase<T> {
	private List<FactorySupplyRule<T>> list = new ArrayList<FactorySupplyRule<T>>();
	private TFSequence<T> factory;	
	
	public FSRStringSequence(String name, RuleSupplyFlag flag) {
		super(flag);
		this.factory = new TFSequence<T>(name);
	}
	
	public void add(FactorySupplyRule<T> r) {
		this.list.add(r);
	}
	
	@Override
	public String getName() {
		return this.factory.getName();
	}
	
	@Override
	public FactorySupplyRule<T> getLeading(RulesByName<T> names, int level) {
		if (level == 0) {
			return this.list.get(0).getLeading(names, 1);
		} else {
			return this;
		}
	}
	
	@Override
	public boolean update(RulesByName<T> symbols) {
		RulesByNameLocal<T> localSymbols = new RulesByNameLocal<T>(symbols, this);
		
		this.factory.reset(this.list.size());
		for (FactorySupplyRule<T> spg : this.list) {
			TokenFactory<T> f = spg.getTheFactory(localSymbols);
			boolean b = spg.getRequired();
			this.factory.add(f, b);
		}

		for (FactorySupplyRule<T> spg : this.list) {
			spg.update(localSymbols);
		}		
		
		return true;		
	}

	@Override
	public TFSequence<T> getShellFactory() {
		return this.factory;
	}
	
	@Override
	public int getSequenceCount() {
		return this.list.size();
	}
}