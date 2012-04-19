package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.ITokenFactory;

public class ChoiceSupply {
	public static ITokenFactory get(ITokenFactory... factories) {
		return new TFChoice(factories);
	}

	public static ITokenFactory get(ITokenFactory defaultFactory, String keys, ITokenFactory... factories) {
		return new TFChoiceOnChar0th(defaultFactory, keys, factories);
	}

	public static ITokenFactory get(ITokenFactory defaultFactory, char key, ITokenFactory factory) {
		return new TFChoiceOnChar0th(defaultFactory, String.valueOf(key), factory);
	}
}
