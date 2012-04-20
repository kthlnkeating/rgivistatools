package com.raygroupintl.bnf;

import com.raygroupintl.struct.CharPredicate;
import com.raygroupintl.vista.fnds.ICharPredicate;
import com.raygroupintl.vista.fnds.ITokenFactory;

public class ChoiceSupply {
	public static ITokenFactory get(ITokenFactory... factories) {
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
	
	public static ITokenFactory get(ITokenFactory defaultFactory,  ICharPredicate[] preds, ITokenFactory... factories) {
		return new TFChoiceOnChar0th(defaultFactory, preds, factories);
	}

	public static ITokenFactory get(ITokenFactory defaultFactory, String keys, ITokenFactory... factories) {
		return new TFChoiceOnChar0th(defaultFactory, toPredicate(keys), factories);
	}

	public static ITokenFactory get(char leadingChar, ITokenFactory defaultFactory, ICharPredicate[] preds, ITokenFactory... factories) {
		return new TFChoiceOnChar1st(leadingChar, defaultFactory, preds, factories);
	}

	public static ITokenFactory get(char leadingChar, ITokenFactory defaultFactory, String keys, ITokenFactory... factories) {
		return new TFChoiceOnChar1st(leadingChar, defaultFactory, toPredicate(keys), factories);
	}

	public static ITokenFactory get(ITokenFactory defaultFactory, char key, ITokenFactory factory) {
		CharPredicate[] predicate = {new CharPredicate(key)};
		return new TFChoiceOnChar0th(defaultFactory, predicate, factory);
	}
}
