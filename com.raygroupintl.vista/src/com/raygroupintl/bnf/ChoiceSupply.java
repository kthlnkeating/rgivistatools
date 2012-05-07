package com.raygroupintl.bnf;

import com.raygroupintl.fnds.ICharPredicate;
import com.raygroupintl.struct.CharPredicate;

public class ChoiceSupply {
	public static TokenFactory get(TokenFactory... factories) {
		return new TFChoiceBasic(factories);
	}

	private static ICharPredicate[] toPredicate(String keys) {
		int n = keys.length();
		ICharPredicate[] result = new ICharPredicate[n];
		for (int i=0; i<n; ++i) {
			result[i] = new CharPredicate(keys.charAt(i));
		}
		return result;
	}
	
	public static TokenFactory get(TokenFactory defaultFactory,  ICharPredicate[] preds, TokenFactory... factories) {
		return new TFChoiceOnChar0th(defaultFactory, preds, factories);
	}

	public static TokenFactory get(TokenFactory defaultFactory, String keys, TokenFactory... factories) {		
		return new TFChoiceOnChar0th(defaultFactory, toPredicate(keys), factories);
	}

	public static TokenFactory get(char leadingChar, TokenFactory defaultFactory, ICharPredicate[] preds, TokenFactory... factories) {
		return new TFChoiceOnChar1st(leadingChar, defaultFactory, preds, factories);
	}

	public static TokenFactory get(char leadingChar, TokenFactory defaultFactory, String keys, TokenFactory... factories) {
		return new TFChoiceOnChar1st(leadingChar, defaultFactory, toPredicate(keys), factories);
	}

	public static TokenFactory get(TokenFactory defaultFactory, char key, TokenFactory factory) {
		CharPredicate[] predicate = {new CharPredicate(key)};
		return new TFChoiceOnChar0th(defaultFactory, predicate, factory);
	}
}
