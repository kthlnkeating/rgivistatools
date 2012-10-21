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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.parser.TFChoice;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.AdapterSpecification;
import com.raygroupintl.parsergen.ruledef.RuleSupplyFlag;

public class FSRChoice extends FSRCollection {
	private static class ForkAlgorithm {	
		private String appliedOnName;
		
		private List<FactorySupplyRule> list = new ArrayList<FactorySupplyRule>();
		
		private Map<String, Integer> choiceOrder = new HashMap<String, Integer>();
		private Map<Integer, String> leadingShared = new HashMap<Integer, String>();
		
		public ForkAlgorithm(String name) {
			this.appliedOnName = name;
		}
	 	
		public void add(FactorySupplyRule tf, RulesByName symbols) {
			FactorySupplyRule leading = tf.getLeading(symbols, 0);
			String name = leading.getName();
			Integer existing = this.choiceOrder.get(name);
			if (existing == null) {
				int n = this.list.size();
				this.list.add(tf);
				this.choiceOrder.put(name, n);
			} else {
				this.leadingShared.put(existing, name);
				int n = existing.intValue();
				FactorySupplyRule current = this.list.get(n);
				if (current instanceof FSRForkedSequence) {
					((FSRForkedSequence) current).addFollower(tf);
				} else {
					FSRForkedSequence newForked = new FSRForkedSequence(this.appliedOnName + "." + name, leading);
					newForked.addFollower(current);
					newForked.addFollower(tf);
					this.list.set(n, newForked);
				}
			}
		}	
	}

	private TFChoice factory;
	
	public FSRChoice(String name, RuleSupplyFlag flag) {
		super(flag);
		this.factory = new TFChoice(name);
	}
	
	@Override
	public String getName() {
		return this.factory.getName();
	}
	
	private List<TokenFactory> getChoiceFactories(RulesByName symbols) {
		List<TokenFactory> result = new ArrayList<TokenFactory>();
		
		ForkAlgorithm algorithm = new ForkAlgorithm(this.getName());
		for (FactorySupplyRule r : this.list) {
			FactorySupplyRule ar = r.getActualRule(symbols);
			algorithm.add(ar, symbols);
		}
		for (FactorySupplyRule on : algorithm.list) {
			on.update(symbols);
			result.add(on.getTheFactory(symbols));
		}
		return result;
	}
	
	@Override
	public boolean update(RulesByName symbols) {
		RulesByNameLocal localSymbols = new RulesByNameLocal(symbols, this);
		List<TokenFactory> fs = this.getChoiceFactories(localSymbols);
		this.factory.reset(fs.size());
		for (TokenFactory f : fs) {
			this.factory.add(f);
		}
		return true;
	}

	@Override
	public TFChoice getShellFactory() {
		return this.factory;	
	}
	
	@Override
	public void setAdapter(AdapterSpecification<Token> spec) {
		Class<? extends Token> a = spec.getTokenAdapter();
		if (a != null) this.factory.setTargetType(a, Token.class);
	}
}
