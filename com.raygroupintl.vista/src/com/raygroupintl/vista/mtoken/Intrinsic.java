package com.raygroupintl.vista.mtoken;

import java.util.List;

import com.raygroupintl.vista.struct.MError;


public abstract class Intrinsic extends MSpecial {
	public Intrinsic(String identifier) {
		super(identifier);
	}

	@Override
	public List<MError> getErrors() {
		return null;
	}

	@Override
	public String getPrefixString() {
		return "$";
	}

	@Override
	public boolean isError() {
		return false;
	}

}
