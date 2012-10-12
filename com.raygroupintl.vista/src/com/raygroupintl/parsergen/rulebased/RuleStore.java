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
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.DelimitedListTokenType;
import com.raygroupintl.parsergen.ParseErrorException;
import com.raygroupintl.parsergen.ParseException;
import com.raygroupintl.parsergen.SequenceTokenType;
import com.raygroupintl.parsergen.TokenFactoryStore;
import com.raygroupintl.parsergen.TokenType;
import com.raygroupintl.parsergen.ruledef.RuleParser;
import com.raygroupintl.parsergen.ruledef.RuleSupply;
import com.raygroupintl.parsergen.ruledef.RuleSupplyFlag;

public class RuleStore extends TokenFactoryStore {
	private static RuleParser ruleParser;
	
	public Map<String, TokenFactory> symbols = new HashMap<String, TokenFactory>();
	
	private java.util.List<FactorySupplyRule> rules  = new ArrayList<FactorySupplyRule>();
	private Map<String, RuleSupply> ruleSupplies  = new HashMap<String, RuleSupply>();
	private Map<String, FactorySupplyRule> topRules  = new HashMap<String, FactorySupplyRule>();
	
	//private FSRVisitingTFStore tfStore = new FSRVisitingTFStore();
	
	public RuleStore() {
	}
	
	private void updateAdapter(Field f, TokenFactory target)  {
		TokenType tokenType = f.getAnnotation(TokenType.class);
		if (tokenType != null) {
			target.setTargetType(tokenType.value());
		}
		SequenceTokenType seqTokenType = f.getAnnotation(SequenceTokenType.class);
		if (seqTokenType != null) {
			target.setSequenceTargetType(seqTokenType.value());
		}
		DelimitedListTokenType dlTokenType = f.getAnnotation(DelimitedListTokenType.class);
		if (dlTokenType != null) {
			target.setDelimitedListTargetType(dlTokenType.value());
		}
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
		FactorySupplyRule topRule = this.topRules.get(name);	
		if (topRule == null) {
			topRule = ruleSupply.getRule(RuleSupplyFlag.TOP, name, this.ruleSupplies);		
			this.topRules.put(name, topRule);
		}
		if (topRule != null) {
			TokenFactory value = topRule.getShellFactory();
			this.rules.add(topRule);
			return value;		
		}
		return null;
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
					this.updateAdapter(f, value);
					f.set(target, value);
				} else {
					return false;
				}
			} else {
				FSRCustom fsr = new FSRCustom(value);
				this.topRules.put(name, fsr);
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
	
	private static void updateRules(java.util.List<FactorySupplyRule> list, java.util.List<FactorySupplyRule> remaining, RulesMapByName tfby) {
		for (FactorySupplyRule r : list) {
			r.update(tfby);
		}
	}

	private void updateRules() {
		java.util.List<FactorySupplyRule> remaining = new ArrayList<FactorySupplyRule>();
		RulesMapByName tfby = new RulesMapByName(this.topRules);
		updateRules(this.rules, remaining, tfby);
		while (remaining.size() > 0) {
			java.util.List<FactorySupplyRule> nextRemaining = new ArrayList<FactorySupplyRule>();
			updateRules(remaining, nextRemaining, tfby);
			if (nextRemaining.size() == remaining.size()) {
				throw new ParseErrorException("There looks to be a circular symbol condition");					
			}
			remaining = nextRemaining;
		}
	}
	
	@Override
	public void addAssumed() {		
		TokenFactory end = new TFEnd("end");
		this.symbols.put("end", end);
		FactorySupplyRule fsr = new FSRCustom(end);
		this.rules.add(fsr);
		this.topRules.put("end", fsr);
	}

	@Override
	public void update(Class<?> cls)  throws IllegalAccessException, InstantiationException, ParseException {
		this.updateRules();
	}		
}

