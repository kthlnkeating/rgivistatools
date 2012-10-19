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

package com.raygroupintl.parsergen.ruledef;

import com.raygroupintl.charlib.Predicate;
import com.raygroupintl.charlib.PredicateFactory;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.Tokens;

public class TCharSymbol extends TSequence implements CharSymbol {
	public TCharSymbol(int length) {
		super(length);
	}
	
	public TCharSymbol(Tokens store) {
		super(store.toList());
	}
	
	private static char getChar(String inQuotes) {
		char ch0th = inQuotes.charAt(1);
		if (ch0th == '\\') {
			switch (inQuotes.charAt(2)) {
				case '\'': return '\'';
				case 'n': return '\n';
				case 'r': return '\r';
				case 't': return '\t';
				default: return ch0th;
			}
		} else {
			return ch0th;
		}
	}
	
	public static void update(PredicateFactory pf, Token sign, Tokens spec) {
		boolean exclude = (sign != null) && (sign.toValue().charAt(0) == '-');
		char ch = getChar(spec.getToken(0).toValue().toString());
		Token tRangeBound = spec.getToken(1);
		if (tRangeBound == null) {
			if (exclude) {
				pf.removeChar(ch);
			} else {
				pf.addChar(ch);
			}
		} else {			
			char chOther = getChar(((Tokens) tRangeBound).getToken(1).toValue().toString());
			if (exclude) {
				pf.removeRange(ch, chOther);
			} else {
				pf.addRange(ch, chOther);
			}
		}
	}
	
	@Override
	public String getKey() {
		return this.toValue().toString();
	}
	
	@Override
	public Predicate getPredicate() {
		PredicateFactory pf = new PredicateFactory();
		TCharSymbol.update(pf, this.getToken(0), (Tokens) this.getToken(1));
		Tokens list = (Tokens) this.getToken(2);
		if (list != null) for (Token t : list) {
			Tokens casted = (Tokens) t;
			TCharSymbol.update(pf, casted.getToken(0), (Tokens) casted.getToken(1));
		}		
		return pf.generate();		
	}
	
	@Override
	public void accept(RuleDefinitionVisitor visitor, String name, RuleSupplyFlag flag) {
		visitor.visitCharSymbol(this, name, flag);
	}
}
