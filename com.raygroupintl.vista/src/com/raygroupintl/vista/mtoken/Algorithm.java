package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public class Algorithm {

	public static interface StopPredicate {
		boolean evaluate(String line, int index);
	}

	public static abstract class StopOnCharPredicate implements StopPredicate {
		@Override
		public boolean evaluate(String line, int index) {
			char ch = line.charAt(index);
			return this.evaluate(ch);
		}

		public abstract boolean evaluate(char ch);		
	}
	
	
	private StopPredicate predicate;
	
	public Algorithm() {		
	}
	
	public Algorithm(StopPredicate predicate) {
		this.predicate = predicate;
	}
		
	public enum CharType {
		LETTER, LETTER_DIGIT
	}
	
	public void setStopChar(final char stopChar) {
		this.predicate = new StopOnCharPredicate() {			
			@Override
			public boolean evaluate(char ch) {
				return stopChar == ch;
			}
		};
	}
	
	public void setContinueChar(final char continueChar) {
		this.predicate = new StopOnCharPredicate() {			
			@Override
			public boolean evaluate(char ch) {
				return continueChar != ch;
			}
		};
	}
	
	public void setContinueChars(final char continueChar1, final char continueChar2) {
		this.predicate = new StopOnCharPredicate() {			
			@Override
			public boolean evaluate(char ch) {
				return (continueChar1 != ch) && (continueChar2 != ch);
			}
		};
	}
	
	public void setStopCharType(CharType charType) {
		switch (charType) {
		case LETTER:
			this.predicate = new StopOnCharPredicate() {		
				@Override
				public boolean evaluate(char ch) {
					return Character.isLetter(ch);
				}
			};
			break;
		case LETTER_DIGIT:
			this.predicate = new StopOnCharPredicate() {		
				@Override
				public boolean evaluate(char ch) {
					return Character.isLetterOrDigit(ch);
				}
			};
			break;
		}
	}
	
	public void setContinueCharType(CharType charType) {
		switch (charType) {
		case LETTER:
			this.predicate = new StopOnCharPredicate() {		
				@Override
				public boolean evaluate(char ch) {
					return ! Character.isLetter(ch);
				}
			};
			break;
		case LETTER_DIGIT:
			this.predicate = new StopOnCharPredicate() {		
				@Override
				public boolean evaluate(char ch) {
					return ! Character.isLetterOrDigit(ch);
				}
			};
			break;
		}
	}	
	
	public int find(String line, int fromIndex) {
		int length = line.length();
		int index = fromIndex;
		while (index < length) {
			if (this.predicate.evaluate(line, index)) {
				return index;
			}
			++index;
		} 
		return length;
	}		

	private boolean evaluatePredicate(String line, int index) {
		if (this.predicate != null) {
			return this.predicate.evaluate(line, index);
		} else {
			return false;
		}
	}
	
	public Multi tokenize(String line, int fromIndex) {
		Multi tokens = new Multi();
		int length = line.length();
		int index = fromIndex;
		while (index < length) {
			char ch = line.charAt(index);
			if (this.evaluatePredicate(line, index)) break;
			ITokenFactory factory = FactorySupply.findFactory(ch);
			IToken token = factory.tokenize(line, index);
			tokens.add(token);
			int resultSize = token.getStringSize();
			assert(resultSize > 0);
			index += resultSize;
		}
		return tokens;
	}
	
	public static Multi tokenize(String line, int fromIndex, char stopChar) {
		Algorithm ta = new Algorithm();
		ta.setStopChar(stopChar);
		return ta.tokenize(line, fromIndex);
	}

	public static Multi tokenize(String line, int fromIndex, StopPredicate predicate) {
		Algorithm ta = new Algorithm(predicate);
		return ta.tokenize(line, fromIndex);
	}

	public static int find(String line, int fromIndex, char ch) {
		Algorithm ta = new Algorithm();
		ta.setStopChar(ch);
		return ta.find(line, fromIndex);
	}

	public static int findOther(String line, int fromIndex, char ch) {
		Algorithm ta = new Algorithm();
		ta.setContinueChar(ch);
		return ta.find(line, fromIndex);
	}

	public static int findOther(String line, int fromIndex, char ch1, char ch2) {
		Algorithm ta = new Algorithm();
		ta.setContinueChars(ch1, ch2);
		return ta.find(line, fromIndex);
	}

	public static int findOther(String line, int fromIndex, CharType charType) {
		Algorithm ta = new Algorithm();
		ta.setContinueCharType(charType);
		return ta.find(line, fromIndex);
	}

}
