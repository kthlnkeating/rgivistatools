package com.raygroupintl.m.token;

import java.util.Iterator;

import com.raygroupintl.m.parsetree.ErrorNode;
import com.raygroupintl.m.parsetree.Line;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.NodeList;
import com.raygroupintl.m.parsetree.ParentNode;
import com.raygroupintl.parser.StringPiece;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.Tokens;

public class MLine extends MSequence {
	String tagName = "";
	int index = 0;

	public MLine(int length) {
		super(length);
	}

	public MLine(Tokens store) {
		super(store);
	}

	public String getTag() {
		Token tag = this.get(0);
		if (tag == null) {
			return null;
		} else {
			return tag.toValue().toString();
		}
	}
	
	public String[] getParameters() {
		MToken paramsTokenWPars = this.getSubNodeToken(1);
		if (paramsTokenWPars != null) {
			Token paramTokens = paramsTokenWPars.getSubNodeToken(1);
			if (paramTokens != null) {
				Tokens resultAsList = (Tokens) paramTokens;
				int length = resultAsList.size();
				if (length > 0) {
					String[] result = new String[resultAsList.size()];
					int i=0;
					for (Token t : resultAsList) {
						result[i] = t.toValue().toString();
						++i;
					}
					return result;
				}
			}
		}
		return null;
	}
	
	public int getLevel() {
		int level = 0;
		Token levelToken = this.get(3);
		if (levelToken != null) {
			StringPiece levelTokenValue = levelToken.toValue();
			return levelTokenValue.count('.');
		}		
		return level;
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
	
	public Line getErrorNode(ErrorNode errorNode) {
		Line result = new Line(this.tagName, this.index, this.getLevel());
		NodeList<Node> nodes = new NodeList<Node>(1);
		nodes.add(errorNode);
		result.setNodes(null);
		return result;
	}
	
	public Line getAsErrorNode(ErrorNode errorNode) {
		Line result = new Line(this.tagName, this.index, this.getLevel());
		NodeList<Node> nodes = new NodeList<Node>(1);
		nodes.add(errorNode);
		result.setNodes(nodes);
		return result;
	}
	
	@Override
	public Line getNode() {
		Line result = new Line(this.tagName, this.index, this.getLevel());
		ParentNode currentParent = result;
		Tokens cmds = (Tokens) this.get(4);
		if (cmds != null) {
			NodeList<Node> nodes = null;
			for (Iterator<Token> it = cmds.iterator(); it.hasNext();) {
				Token t = it.next();
				Node node = ((MToken) t).getNode();
				if (node != null) {
					if (nodes == null) nodes = new NodeList<Node>(cmds.size());
					currentParent = node.addSelf(currentParent, nodes);
				}
			}
			if ((nodes != null) && (nodes.size() > 0)) {
				currentParent.setNodes(nodes.copy());
			}
		}
		return result;
	}
}
