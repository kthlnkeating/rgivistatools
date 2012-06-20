package com.raygroupintl.m.token;

import java.util.Iterator;
import java.util.List;

import com.raygroupintl.m.parsetree.Line;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.NodeList;
import com.raygroupintl.m.parsetree.ParentNode;
import com.raygroupintl.parser.StringPiece;
import com.raygroupintl.parser.TList;
import com.raygroupintl.parser.Token;

public class MLine extends MSequence {
	String tagName = "";
	int index = 0;

	public MLine(Token token) {
		super(token);
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
		Token paramsTokenWPars = this.get(1);
		if (paramsTokenWPars != null) {
			Token paramTokens = paramsTokenWPars.toList().get(1);
			if (paramTokens != null) {
				List<Token> resultAsList = paramTokens.toList();
				int length = resultAsList.size();
				if (length > 0) {
					String[] result = new String[resultAsList.size()];
					for (int i=0; i<length; ++i) {
						result[i] = resultAsList.get(i).toValue().toString();
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
	
	@Override
	public Line getNode() {
		Line result = new Line(this.tagName, this.index, this.getLevel());
		ParentNode currentParent = result;
		TList cmds = (TList) this.get(4);
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
