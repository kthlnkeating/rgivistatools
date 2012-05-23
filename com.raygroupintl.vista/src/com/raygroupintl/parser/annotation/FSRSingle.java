package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TokenFactory;

public class FSRSingle extends FSRBase implements TopTFRule {
	private String value;
	
	public FSRSingle(String value, boolean required) {
		super(required);
	}
	
	@Override
	public TokenFactory getFactory(String name, Map<String, TokenFactory> symbols) {
		TokenFactory result = symbols.get(this.value);
		if (result == null) throw new ParseErrorException("Undefined symbol " + value + " used in the rule");
		if (! result.isInitialized()) {
			return null;
		}
		return result;
	}

	@Override
	public TFBasic getTopFactory(String name, Map<String, TokenFactory> symbols, boolean asShell) {
		TokenFactory source = symbols.get(this.value);
		if (source == null) {
			if (! asShell) throw new ParseErrorException("Undefined symbol " + this.value + " used in the rule");
			return null;
		}
		if (source instanceof TFBasic) {
			return ((TFBasic) source).getCopy(name);
		} else {
			throw new ParseErrorException("Custom symbol " + this.value + " cannot be used as a top symbol in rules");
		}
	}
}