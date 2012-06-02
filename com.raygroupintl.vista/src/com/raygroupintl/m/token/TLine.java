package com.raygroupintl.m.token;

import java.util.Iterator;
import java.util.List;

import com.raygroupintl.m.parsetree.Line;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.parser.TList;
import com.raygroupintl.parser.Token;

public class TLine extends MTSequence {
	String tagName = "";
	int index = 0;

	public TLine(List<Token> tokens) {
		super(tokens);
	}

	public String getTag() {
		Token tag = this.get(0);
		if (tag == null) {
			return null;
		} else {
			return tag.toValue().toString();
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
	
	@Override
	public Line getNode() {
		Line result = new Line(this.tagName, this.index, this.getLevel());
		TList cmds = (TList) this.get(4);
		if (cmds != null) {
			for (Iterator<Token> it = cmds.iterator(); it.hasNext();) {
				Token t = it.next();
				if (t instanceof MToken) {
					Node node = ((MToken) t).getNode();
					result.add(node);
				}
			}
		}
		return result;
	}
}
