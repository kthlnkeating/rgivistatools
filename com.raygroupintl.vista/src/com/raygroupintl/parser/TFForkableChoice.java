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

package com.raygroupintl.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.parser.annotation.AdapterSupply;
import com.raygroupintl.parser.annotation.TokenFactoriesByName;

public class TFForkableChoice extends TFBasic {
	private List<TokenFactory> factories = new ArrayList<TokenFactory>();
	
	private Map<String, Integer> choiceOrder = new HashMap<String, Integer>();
	private Map<Integer, List<String>> possibleShared = new HashMap<Integer, List<String>>();
	private Set<String> restrictedChoices = new HashSet<String>();
	private Map<Integer, TokenFactory> leadingShared = new HashMap<Integer,TokenFactory>();
	
	public TFForkableChoice(String name) {
		super(name);
	}
	
	private void updateChoicePossibilities(TokenFactory f, TokenFactoriesByName symbols, int index) {
		TokenFactory previous = null;
		List<String> allForIndex = new ArrayList<String>();
		while (f != previous) {
			String name = f.getName();
			if (! restrictedChoices.contains(name)) {
				if (symbols.get(name) != null) {
					this.choiceOrder.put(name, index);
					allForIndex.add(name);
				}
			}
			previous = f;
			f = f.getLeadingFactory();
		}
		this.possibleShared.put(index, allForIndex);
	}
	
	private Integer findInChoices(TokenFactory f, TokenFactoriesByName symbols) {
		TokenFactory previous = null;
		while (f != previous) {
			String name = f.getName();
			Integer order = this.choiceOrder.get(name);
			if (order != null) {
				//boolean found = false;
				if (this.possibleShared.containsKey(order)) {
					for (String r : this.possibleShared.get(order)) {
						if (! r.equals(name)) {
							this.choiceOrder.remove(r);
							this.restrictedChoices.add(r);
						} //else {
						//	found = r.equals(name);
						//}
					}
					this.leadingShared.put(order, symbols.get(name));
					this.possibleShared.remove(order);
				}
				return order;
			}
			previous = f;
			f = f.getLeadingFactory();
		}
		return null;
	}
	
	public void reset() {
		this.factories = new ArrayList<TokenFactory>();
		this.choiceOrder = new HashMap<String, Integer>();
		this.possibleShared = new HashMap<Integer, List<String>>();
		this.restrictedChoices = new HashSet<String>();
		this.leadingShared = new HashMap<Integer,TokenFactory>();		
	}
	
	public void add(TokenFactory tf, TokenFactoriesByName symbols) {
		Integer existing = this.findInChoices(tf, symbols);
		if (existing == null) {
			int n = this.factories.size();
			this.factories.add(tf);
			this.updateChoicePossibilities(tf, symbols, n);
		} else {
			int n = existing.intValue();
			TokenFactory current = this.factories.get(n);
			if (current instanceof TFForkedSequence) {
				((TFForkedSequence) current).addFollower(tf);
			} else {
				TokenFactory leading = this.leadingShared.get(n);
				String name = leading.getName();			
				TFForkedSequence newForked = new TFForkedSequence(this.getName() + "." + name, leading);
				newForked.addFollower(current);
				newForked.addFollower(tf);
				this.factories.set(n, newForked);
			}
		}
	}
	
	
	@Override
	public Token tokenize(Text text, AdapterSupply adapterSupply) throws SyntaxErrorException {
		if (text.onChar()) {
			for (TokenFactory f : this.factories) {
				Token result = f.tokenize(text, adapterSupply);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}
	
	public void setTargetType(Class<? extends Token> cls) {
		//throw new UnsupportedOperationException("setTargetType is not implemented for " + TFForkableChoice.class.getName());		
	}
	
	public void setAdapter(Object adapter) {
		//throw new UnsupportedOperationException("setaDAPTER is not implemented for " + TFForkableChoice.class.getName());				
	}
	
	public void copyWoutAdapterFrom(TFBasic rhs) {
		if (rhs instanceof TFForkableChoice) {
			TFForkableChoice rhsCasted = (TFForkableChoice) rhs;
			this.factories = rhsCasted.factories;
			this.choiceOrder = rhsCasted.choiceOrder;
		} else {
			throw new IllegalArgumentException("Illegal attemp to copy from " + rhs.getClass().getName() + " to " + TFForkableChoice.class.getName());
		}
		
	}
	
	public TFBasic getCopy(String name) {
		throw new UnsupportedOperationException("GetCopy is not implemented for " + TFForkableChoice.class.getName());
	}

	@Override
	public boolean isInitialized() {
		if (this.factories.size() > 0) {
			for (TokenFactory f : this.factories) {
				if (f instanceof TFBasic) {
					if (! ((TFBasic) f).isInitialized()) return false;
				}
			}
			return true;
		} else {
			return false;
		}
		
	}
}
