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

import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.AdapterSpecification;
import com.raygroupintl.parsergen.ParseErrorException;
import com.raygroupintl.parsergen.ruledef.RuleSupplyFlag;

public abstract class FSRBase<T extends Token> implements FactorySupplyRule<T> {
	private RuleSupplyFlag flag;
	
	public FSRBase(RuleSupplyFlag flag) {
		this.flag = flag;
	}
	
	@Override
	public boolean getRequired() {
		switch (this.flag) {
			case INNER_OPTIONAL: 
				return false;
			case INNER_REQUIRED: 
				return true;
			default:
				throw new ParseErrorException("Internal error: attempt to get required flag for a top symbol.");
		}
	}
	
	@Override
	public FactorySupplyRule<T> formList(String name, RuleSupplyFlag flag, ListInfo<T> info) {
		if (info.delimiter == null) {
			return new FSRList<T>(name, flag, this);
		}
		if ((info.left == null) || (info.right == null)) {
			return new FSRDelimitedList<T>(name, flag, this, info.delimiter);		
		}
		{
			FSREnclosedDelimitedList<T> result = new FSREnclosedDelimitedList<T>(name, flag, this, info.delimiter, info.left, info.right);
			result.setEmptyAllowed(info.emptyAllowed);
			result.setNoneAllowed(info.noneAllowed);
			return result;
		}		
	}

	@Override
	public boolean update(RulesByName<T> symbols) {
		return true;
	}

	public TokenFactory<T> getTheFactory(RulesByName<T> symbols) {
		FactorySupplyRule<T> af = this.getActualRule(symbols);
		return af.getShellFactory();
	}

	@Override
	public FactorySupplyRule<T> getLeading(RulesByName<T> names, int level) {
		return this;
	}
	
	@Override
	public FactorySupplyRule<T> getActualRule(RulesByName<T> symbols) {
		return this;
	}
	
	@Override
	public int getSequenceCount() {
		return 1;
	}

	@Override
	public void setAdapter(AdapterSpecification<T> spec) {
		spec.getNull();
	}
	
	@Override
	public String[] getNeededNames() {
		return new String[0];
	}
}
