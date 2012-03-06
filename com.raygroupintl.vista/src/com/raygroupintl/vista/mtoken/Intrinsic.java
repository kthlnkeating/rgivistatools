package com.raygroupintl.vista.mtoken;


public abstract class Intrinsic extends MSpecial {
	public Intrinsic(String identifier) {
		super(identifier);
	}

	@Override
	public String getPrefixString() {
		return "$";
	}
}
