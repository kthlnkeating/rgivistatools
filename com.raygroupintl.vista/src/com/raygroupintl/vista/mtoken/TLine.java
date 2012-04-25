package com.raygroupintl.vista.mtoken;

import java.util.List;

import com.raygroupintl.bnf.TArray;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.m.struct.Fanout;

public class TLine extends TArray {	
	public TLine(IToken[] tokens) {
		super(tokens);
	}

	public String getTag() {
		IToken tag = this.get(0);
		if (tag == null) {
			return null;
		} else {
			return tag.getStringValue();
		}
	}
	
	public List<Fanout> getFanouts() {
		return null;
	}
}
