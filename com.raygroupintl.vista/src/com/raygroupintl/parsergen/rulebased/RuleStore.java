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

public class RuleStore extends TokenFactoryStore  {
	private static RuleParser ruleParser;
	
	public Map<String, TokenFactory> symbols = new HashMap<String, TokenFactory>();
	
	private java.util.List<FactorySupplyRule> rules  = new ArrayList<FactorySupplyRule>();
	private Map<String, RuleSupply> ruleSupplies  = new HashMap<String, RuleSupply>();
	
	private DefinitionVisitor dv = new DefinitionVisitor();
	
	public RuleStore() {
	}
	
	private TokenFactory addRule(String name, Rule ruleAnnotation, Field f) {
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
		FactorySupplyRule topRule = this.dv.topRules.get(name);	
		if (topRule == null) {
			ruleSupply.accept(this.dv, name, RuleSupplyFlag.TOP);
			topRule = this.dv.topRules.get(name);
		}

		AdapterSpecification<Token> spec = AdapterSpecification.getInstance(f, Token.class);
		topRule.setAdapter(spec);
		TokenFactory value = topRule.getShellFactory();
		this.rules.add(topRule);
		return value;
	}
	
	@Override
	protected TokenFactory add(Field f)  {
		String name = f.getName();			
		Rule description = f.getAnnotation(Rule.class);
		if (description != null) {
			return this.addRule(name, description, f);
		} 
		return null;
	}
	
	protected <T> boolean handleField(T target, Field f) throws IllegalAccessException {
		String name = f.getName();
		TokenFactory already = this.symbols.get(name);
		if (already == null) {					
			TokenFactory value = (TokenFactory) f.get(target);
			if (value == null) {
				value = this.add(f);
				if (value != null) {
					f.set(target, value);
				} else {
					return false;
				}
			} else {
				FSRCustom fsr = new FSRCustom(value);
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
		TokenFactory end = new TFEnd("end");
		this.symbols.put("end", end);
		FactorySupplyRule fsr = new FSRCustom(end);
		this.rules.add(fsr);
		this.dv.topRules.put("end", fsr);
	}

	@Override
	public void update(Class<?> cls) throws ParseException {
		RulesMapByName tfby = new RulesMapByName(this.dv.topRules);

		String errorSymbols = "";
		for (FactorySupplyRule r : this.rules) {
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
		
		for (FactorySupplyRule r : this.rules) {
			boolean result = r.update(tfby);			
			assert(result);
		}
	}		
}

