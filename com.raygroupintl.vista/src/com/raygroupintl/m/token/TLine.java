package com.raygroupintl.m.token;

import java.util.List;

import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TArray;
import com.raygroupintl.m.struct.Fanout;

public class TLine extends TArray {	
	public TLine(Token[] tokens) {
		super(tokens);
	}

	public String getTag() {
		Token tag = this.get(0);
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
