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

import com.raygroupintl.charlib.PredicateFactory;
import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenStore;
import com.raygroupintl.parsergen.rulebased.FSRChar;
import com.raygroupintl.parsergen.rulebased.FactorySupplyRule;

public class TCharSymbol extends TSequence implements RuleSupply {
	public TCharSymbol(int length) {
		super(length);
	}
	
	public TCharSymbol(TokenStore store) {
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
	
	private static void update(PredicateFactory pf, Token sign, TokenStore spec) {
		boolean exclude = (sign != null) && (sign.toValue().charAt(0) == '-');
		char ch = getChar(spec.get(0).toValue().toString());
		Token tRangeBound = spec.get(1);
		if (tRangeBound == null) {
			if (exclude) {
				pf.removeChar(ch);
			} else {
				pf.addChar(ch);
			}
		} else {			
			char chOther = getChar(((TokenStore) tRangeBound).get(1).toValue().toString());
			if (exclude) {
				pf.removeRange(ch, chOther);
			} else {
				pf.addRange(ch, chOther);
			}
		}
	}
	
	@Override
	public FactorySupplyRule getRule(RuleSupplyFlag flag, String name) {
		PredicateFactory pf = new PredicateFactory();
		update(pf, this.get(0), (TokenStore) this.get(1));
		TokenStore list = (TokenStore) this.get(2);
		if (list != null) for (Token t : list) {
			TokenStore casted = (TokenStore) t;
			update(pf, casted.get(0), (TokenStore) casted.get(1));
		}		

		String key = this.toValue().toString();
		return new FSRChar(key, flag, pf.generate());
	}
}
