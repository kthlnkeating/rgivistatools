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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.parser.TFChoice;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.AdapterSpecification;
import com.raygroupintl.parsergen.ruledef.RuleSupplyFlag;

public class FSRChoice<T extends Token> extends FSRCollection<T> {
	private static class ForkAlgorithm<T extends Token> {	
		private String appliedOnName;
		
		private List<FactorySupplyRule<T>> list = new ArrayList<FactorySupplyRule<T>>();
		
		private Map<String, Integer> choiceOrder = new HashMap<String, Integer>();
		private Map<Integer, String> leadingShared = new HashMap<Integer, String>();
		
		public ForkAlgorithm(String name) {
			this.appliedOnName = name;
		}
	 	
		public void add(FactorySupplyRule<T> tf, RulesByName<T> symbols) {
			FactorySupplyRule<T> leading = tf.getLeading(symbols, 0);
			String name = leading.getName();
			Integer existing = this.choiceOrder.get(name);
			if (existing == null) {
				int n = this.list.size();
				this.list.add(tf);
				this.choiceOrder.put(name, n);
			} else {
				this.leadingShared.put(existing, name);
				int n = existing.intValue();
				FactorySupplyRule<T> current = this.list.get(n);
				if (current instanceof FSRForkedSequence) {
					((FSRForkedSequence<T>) current).addFollower(tf);
				} else {
					FSRForkedSequence<T> newForked = new FSRForkedSequence<T>(this.appliedOnName + "." + name, leading);
					newForked.addFollower(current);
					newForked.addFollower(tf);
					this.list.set(n, newForked);
				}
			}
		}	
	}

	private TFChoice<T> factory;
	
	public FSRChoice(String name, RuleSupplyFlag flag) {
		super(flag);
		this.factory = new TFChoice<T>(name);
	}
	
	@Override
	public String getName() {
		return this.factory.getName();
	}
	
	private List<TokenFactory<T>> getChoiceFactories(RulesByName<T> symbols) {
		List<TokenFactory<T>> result = new ArrayList<TokenFactory<T>>();
		
		ForkAlgorithm<T> algorithm = new ForkAlgorithm<T>(this.getName());
		for (FactorySupplyRule<T> r : this.list) {
			FactorySupplyRule<T> ar = r.getActualRule(symbols);
			algorithm.add(ar, symbols);
		}
		for (FactorySupplyRule<T> on : algorithm.list) {
			on.update(symbols);
			result.add(on.getTheFactory(symbols));
		}
		return result;
	}
	
	@Override
	public boolean update(RulesByName<T> symbols) {
		RulesByNameLocal<T> localSymbols = new RulesByNameLocal<T>(symbols, this);
		List<TokenFactory<T>> fs = this.getChoiceFactories(localSymbols);
		this.factory.reset(fs.size());
		for (TokenFactory<T> f : fs) {
			this.factory.add(f);
		}
		return true;
	}

	@Override
	public TFChoice<T> getShellFactory() {
		return this.factory;	
	}
	
	@Override
	public void setAdapter(AdapterSpecification<T> spec) {
		Constructor<? extends T> constructor = spec.getTokenAdapter();
		if (constructor != null) this.factory.setTargetType(constructor);
	}
}
