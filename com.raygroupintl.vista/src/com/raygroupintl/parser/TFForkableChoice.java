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
import java.util.List;

import com.raygroupintl.parser.annotation.AdapterSupply;
import com.raygroupintl.parser.annotation.RulesByName;

public class TFForkableChoice extends TFBasic {
	private List<TokenFactory> factories = new ArrayList<TokenFactory>();
	private ForkAlgorithm algorithm;
	
	public TFForkableChoice(String name) {
		super(name);
		this.algorithm = new ForkAlgorithm(name);
	}
	
	public List<TokenFactory> getFactories() {
		return this.factories;
	}
	
	public void reset(String name) {
		this.factories = new ArrayList<TokenFactory>();
		this.algorithm = new ForkAlgorithm(name);
	}
	
	public void setFactories(List<TokenFactory> factories) {
		this.factories = factories;
	}
	
	public void add(TokenFactory tf, RulesByName symbols) {
		Integer existing = this.algorithm.findInChoices(tf, symbols);
		if (existing == null) {
			int n = this.factories.size();
			this.factories.add(tf);
			this.algorithm.updateChoicePossibilities(tf, symbols, n);
		} else {
			int n = existing.intValue();
			TokenFactory current = this.factories.get(n);
			if (current instanceof TFForkedSequence) {
				((TFForkedSequence) current).addFollower(tf);
			} else {
				String name = this.algorithm.leadingShared.get(n);
				TokenFactory leading = symbols.get(name).getShellFactory();
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
