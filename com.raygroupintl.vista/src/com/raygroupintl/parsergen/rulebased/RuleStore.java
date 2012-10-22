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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.parser.TFEnd;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.AdapterSpecification;
import com.raygroupintl.parsergen.ParseException;
import com.raygroupintl.parsergen.TokenFactoryStore;
import com.raygroupintl.parsergen.ruledef.RuleParser;
import com.raygroupintl.parsergen.ruledef.RuleSupply;
import com.raygroupintl.parsergen.ruledef.RuleSupplyFlag;

public class RuleStore<T extends Token> extends TokenFactoryStore<T>  {
	private static RuleParser ruleParser;
	
	public Map<String, TokenFactory<T>> symbols = new HashMap<String, TokenFactory<T>>();
	
	private java.util.List<FactorySupplyRule<T>> rules  = new ArrayList<FactorySupplyRule<T>>();
	private Map<String, RuleSupply> ruleSupplies  = new HashMap<String, RuleSupply>();
	
	private DefinitionVisitor<T> dv = new DefinitionVisitor<T>();
	
	public RuleStore() {
	}
	
	private TokenFactory<T> addRule(String name, Rule ruleAnnotation, Field f, Class<T> tokenCls) {
		if (ruleParser == null) {
			ruleParser = new RuleParser();
		}
		RuleSupply ruleSupply = this.ruleSupplies.get(name);
		if (ruleSupply == null) {
			String ruleText = ruleAnnotation.value();
			ruleSupply = ruleParser.getTopTFRule(name, ruleText);
			if (ruleSupply == null) return null;
			this.ruleSupplies.put(name, ruleSupply);
		}
		FactorySupplyRule<T> topRule = this.dv.topRules.get(name);	
		if (topRule == null) {
			ruleSupply.accept(this.dv, name, RuleSupplyFlag.TOP);
			topRule = this.dv.topRules.get(name);
		}

		AdapterSpecification<T> spec = AdapterSpecification.getInstance(f, tokenCls);
		topRule.setAdapter(spec);
		TokenFactory<T> value = topRule.getShellFactory();
		this.rules.add(topRule);
		return value;
	}
	
	@Override
	protected TokenFactory<T> add(Field f, Class<T> tokenCls)  {
		String name = f.getName();			
		Rule description = f.getAnnotation(Rule.class);
		if (description != null) {
			return this.addRule(name, description, f, tokenCls);
		} 
		return null;
	}
	
	protected <M> boolean handleField(M target, Field f, Class<T> tokenCls) throws IllegalAccessException {
		String name = f.getName();
		TokenFactory<T> already = this.symbols.get(name);
		if (already == null) {
			@SuppressWarnings("unchecked")
			TokenFactory<T> value = (TokenFactory<T>) f.get(target);
			if (value == null) {
				value = this.add(f, tokenCls);
				if (value != null) {
					f.set(target, value);
				} else {
					return false;
				}
			} else {
				FSRCustom<T> fsr = new FSRCustom<T>(value);
				this.dv.topRules.put(name, fsr);
				this.rules.add(fsr);
			}
			if (value != null) {
				this.symbols.put(name, value);
			}
		} else {
			f.set(target, already);						
		}
		return true;
	}
	
	@Override
	public void addAssumed() {		
		TokenFactory<T> end = new TFEnd<T>("end");
		this.symbols.put("end", end);
		FactorySupplyRule<T> fsr = new FSRCustom<T>(end);
		this.rules.add(fsr);
		this.dv.topRules.put("end", fsr);
	}

	@Override
	public void update(Class<?> cls) throws ParseException {
		RulesMapByName<T> tfby = new RulesMapByName<T>(this.dv.topRules);

		String errorSymbols = "";
		for (FactorySupplyRule<T> r : this.rules) {
			String[] neededs = r.getNeededNames();
			for (String needed : neededs) {
				if (! tfby.hasRule(needed)) {
					errorSymbols += ", " + needed;					
				}
			}
		}
		if (! errorSymbols.isEmpty()) {
			throw new ParseException("Following symbols are not resolved: " + errorSymbols.substring(1));			
		}
		
		for (FactorySupplyRule<T> r : this.rules) {
			boolean result = r.update(tfby);			
			assert(result);
		}
	}		
}

