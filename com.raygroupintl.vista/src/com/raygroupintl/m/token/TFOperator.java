package com.raygroupintl.m.token;

import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.bnf.StringAdapter;
import com.raygroupintl.bnf.Text;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;

public class TFOperator extends TokenFactory {
	private static class OperatorAdapter implements StringAdapter {
		@Override
		public Token convert(String value) {
			return new TOperator(value);
		}		
	}
		
	private static class OperatorBranch {
		public Map<Character, OperatorBranch> nextBranch = new HashMap<Character, OperatorBranch>();
		public boolean validEnd;
	}
	
	private Map<Character, OperatorBranch> operators = new HashMap<Character, OperatorBranch>();
	
	public void addOperator(String operator) {
		Character ch = operator.charAt(0);
		OperatorBranch branch = this.operators.get(ch);
		if (branch == null) {
			branch = new OperatorBranch();
			this.operators.put(ch, branch);
		}
		for (int i=1; i<operator.length(); ++i) {
			Character nextCharacter = operator.charAt(i);
			OperatorBranch nextBranch = branch.nextBranch.get(nextCharacter);
			if (nextBranch == null) {
				nextBranch = new OperatorBranch();
				 branch.nextBranch.put(nextCharacter, nextBranch);
			}
			branch = nextBranch;
		}
		branch.validEnd = true;
	}
		
	@Override
	public Token tokenize(Text text) {
		if (text.onChar()) {
			char ch = text.getChar();	
			OperatorBranch branch = this.operators.get(ch);
			if (branch != null) {
				int index = 1;
				while (text.onChar(index)) {
					ch = text.getChar(index);
					OperatorBranch nextBranch = branch.nextBranch.get(ch);
					if (nextBranch == null) break;
					branch = nextBranch;
					++index;
				}
				if (branch.validEnd) {
					return text.extractToken(index, new OperatorAdapter());
				}				
			}
		}
		return null;
	}
	
	public static TFOperator getInstance() {
		return new TFOperator();
	}
}
