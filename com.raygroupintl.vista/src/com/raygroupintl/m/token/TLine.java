package com.raygroupintl.m.token;

import java.util.Iterator;
import java.util.List;

import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TArray;
import com.raygroupintl.m.cmdtree.Line;
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
	
	public int getLevel() {
		return 0;
	}

	public List<Fanout> getFanouts() {
		return null;
	}

	public Line getNode(int nodeIndex, Iterator<TLine> followingLines) {
		int level = this.getLevel();
		Line result = new Line(nodeIndex, level);
		return result;
	}
}
