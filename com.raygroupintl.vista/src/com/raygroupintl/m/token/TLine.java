package com.raygroupintl.m.token;

import java.util.Iterator;
import java.util.List;

import com.raygroupintl.bnf.TList;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TArray;
import com.raygroupintl.m.parsetree.Line;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.struct.Fanout;

public class TLine extends TArray implements NodeFactory {
	String tagName = "";
	int index = 0;

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

	public void setIdentifier(String tagName, int index) {
		this.tagName = tagName;
		this.index = index;
	}

	public String getTagName() {
		return this.tagName;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public List<Fanout> getFanouts() {
		return null;
	}

	@Override
	public Line getNode() {
		Line result = new Line(this.tagName, this.index, this.getLevel());
		TList cmds = (TList) this.get(4);
		if (cmds != null) {
			for (Iterator<Token> it = cmds.iteratorForDelimited(); it.hasNext();) {
				Token t = it.next();
				if (t instanceof NodeFactory) {
					Node node = ((NodeFactory) t).getNode();
					result.add(node);
				}
			}
		}
		return result;
	}
}
