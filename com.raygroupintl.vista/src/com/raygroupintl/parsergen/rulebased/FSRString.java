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

import com.raygroupintl.charlib.Predicate;
import com.raygroupintl.parser.TFString;
import com.raygroupintl.parser.TextPiece;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parsergen.AdapterSpecification;
import com.raygroupintl.parsergen.ruledef.RuleSupplyFlag;

public class FSRString extends FSRBase {
	private String expr;
	private Predicate predicate;
	private TFString factory;
	
	public FSRString(String expr, RuleSupplyFlag flag, Predicate predicate) {
		super(flag);
		this.expr = expr;
		this.predicate = predicate;
		this.factory = new TFString(this.expr, this.predicate);
	}
	
	@Override
	public boolean update(RulesByName symbols) {
		if (! symbols.hasRule(expr)) {
			symbols.put(this.expr, this);
		}
		return true;
	}
	
	@Override
	public String getName() {
		return this.expr;
	}
	
	@Override
	public TFString getShellFactory() {
		return this.factory;
	}

	@Override
	public void setAdapter(AdapterSpecification spec) {
		 Class<? extends Token> a = spec.getStringTokenAdapter();
		 if (a != null) this.factory.setStringTargetType(a, TextPiece.class);
	}
}
	
