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

import com.raygroupintl.parser.TFDelimitedList;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.AdapterSpecification;
import com.raygroupintl.parsergen.ruledef.RuleSupplyFlag;

public class FSREnclosedDelimitedList extends FSRBase {
	private FactorySupplyRule element;
	private FactorySupplyRule delimiter;
	private FactorySupplyRule left;
	private FactorySupplyRule right;
	private boolean empty;
	private boolean none;
	private TFSequence factory;
	
	public FSREnclosedDelimitedList(String name, RuleSupplyFlag flag, FactorySupplyRule element, FactorySupplyRule delimiter, FactorySupplyRule left, FactorySupplyRule right) {
		super(flag);
		this.element = element;
		this.delimiter = delimiter;
		this.left = left;
		this.right = right;
		this.factory = new TFSequence(name, 3);
	}
	
	public void setEmptyAllowed(boolean b) {
		this.empty = b;
	}
	
	
	public void setNoneAllowed(boolean b) {
		this.none = b;
	}
		
	@Override
	public String getName() {
		return this.factory.getName();
	}
	
	@Override
	public boolean update(RulesByName symbols) {
		RulesByNameLocal localSymbols = new RulesByNameLocal(symbols, this);
		String name = this.factory.getName();
		this.element.update(localSymbols);
		this.delimiter.update(localSymbols);
		TokenFactory e = this.element.getTheFactory(localSymbols);
		TokenFactory d = this.delimiter.getTheFactory(localSymbols);
		TFDelimitedList dl = new TFDelimitedList(name);		
		dl.set(e, d, this.empty);
		this.left.update(localSymbols);
		this.right.update(localSymbols);
		TokenFactory l = this.left.getTheFactory(localSymbols);
		TokenFactory r = this.right.getTheFactory(localSymbols);
		
		this.factory.reset(4);
		this.factory.add(l, true);
		this.factory.add(dl, ! this.none);
		this.factory.add(r, true);
		return true;		
	}

	@Override
	public TFSequence getShellFactory() {
		return this.factory;
	}

	@Override
	public void setAdapter(AdapterSpecification<Token> spec) {
		 Constructor<? extends Token> a = spec.getSequenceTokenAdapter();
		 if (a != null) {
			 this.factory.setSequenceTargetType(a);
		 } else {
			 Constructor<? extends Token> constructorAlt = spec.getTokenAdapter();
			 if (constructorAlt != null) this.factory.setTargetType(constructorAlt);
		 }
	}	
}