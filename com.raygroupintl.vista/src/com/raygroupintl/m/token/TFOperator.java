package com.raygroupintl.m.token;

import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;

public class TFOperator extends TokenFactory {
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
	public Token tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			char ch = line.charAt(fromIndex);	
			OperatorBranch branch = this.operators.get(ch);
			if (branch != null) {
				int index = fromIndex + 1;
				while (index < endIndex) {
					ch = line.charAt(index);
					OperatorBranch nextBranch = branch.nextBranch.get(ch);
					if (nextBranch == null) break;
					branch = nextBranch;
					++index;
				}
				if (branch.validEnd) {
					return new TOperator(line.substring(fromIndex, index));
				}				
			}
		}
		return null;
	}
	
	public static TFOperator getInstance() {
		return new TFOperator();
	}
}
