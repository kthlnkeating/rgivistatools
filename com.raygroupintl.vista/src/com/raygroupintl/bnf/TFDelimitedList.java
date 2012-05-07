package com.raygroupintl.bnf;


public final class TFDelimitedList implements TokenFactory {
	private static class DLAdapter implements TokenAdapter {
		@Override
		public Token convert(String line, int fromIndex, Token[] tokens) {
			if (tokens[1] == null) {
				return new TList(tokens[0]);	
			} else {		
				TList result = (TList) tokens[1];
				result.add(0, tokens[0]);
				return result;
			}			
		}
	}
		
	private TokenFactory elementFactory;
	private TokenFactory delimiter;
	private TokenFactory left;
	private TokenFactory right;
	private boolean allowEmpty;
	private boolean allowNone;
	
	public TFDelimitedList() {		
	}
	
	public TFDelimitedList(TokenFactory elementFactory) {	
		this.elementFactory = elementFactory;
	}

	public TFDelimitedList(TokenFactory elementFactory, char ch) {	
		this.elementFactory = elementFactory;
		this.delimiter = new TFConstChar(ch);
	}

	public void setElementFactory(TokenFactory elementFactory) {
		this.elementFactory = elementFactory;
	}
	
	private TokenFactory getStringFactory(String value) {
		if (value.length() == 1) {
			char ch = value.charAt(0);
			return new TFConstChar(ch);
		} else {
			return new TFConstString(value);
		}
	}
	
	public void setDelimiter(TokenFactory delimeter) {
		this.delimiter = delimeter;
	}

	public void setDelimiter(String delimeter) {
		TokenFactory tf = this.getStringFactory(delimeter);
		this.setDelimiter(tf);
	}
	
	public void setLeft(TokenFactory left) {
		this.left = left;
	}

	public void setLeft(String left) {
		TokenFactory tf = this.getStringFactory(left);
		this.setLeft(tf);
	}
	
	public void setRight(TokenFactory right) {
		this.right = right;
	}

	public void setRight(String right) {
		TokenFactory tf = this.getStringFactory(right);
		this.setRight(tf);
	}
	
	public void setAllowEmpty(boolean b) {
		this.allowEmpty = b;
	}
	
	public void setAllowNone(boolean b) {
		this.allowNone = b;
	}
	
	private TokenFactory getEffectiveListTailElementFactory() {
		if (this.allowEmpty) {
			TokenFactory eDelimiter = new TFEmpty(this.delimiter);
			if (this.right == null) {
				return new TFChoiceBasic(this.elementFactory, eDelimiter);
			} else {
				TokenFactory eRight = new TFEmpty(this.right);
				return new TFChoiceBasic(this.elementFactory, eDelimiter, eRight);
			}
		} else {
			return this.elementFactory;
		}
	}
	
	private TokenFactory getLeadingElement() {
		if (this.allowEmpty) {
			TokenFactory eDelimiter = new TFEmpty(this.delimiter);
			return new TFChoiceBasic(this.elementFactory, eDelimiter);
		} else {
			return this.elementFactory;
		}
		
	}
	
	private TokenFactory getEffectiveListFactory() {
		if (this.delimiter == null) {
			return new TFList(this.elementFactory);
		} else {
			TokenFactory tailElement = this.getEffectiveListTailElementFactory();
			TFSeqStatic tailSingle = new TFSeqStatic(this.delimiter, tailElement);
			tailSingle.setRequiredFlags(new boolean[]{true, true});
			TokenFactory tail = new TFList(tailSingle);
			TokenFactory leadingElement = this.getLeadingElement();
			TFSeqStatic result = new TFSeqStatic(leadingElement, tail);
			result.setRequiredFlags(new boolean[]{true, false});
			result.setTokenAdapter(new DLAdapter());				
			return result;
		}
	}
	
	@Override
	public Token tokenize(String line, int fromIndex) {
		if (line.length() > fromIndex) {
			TokenFactory tfList = this.getEffectiveListFactory();
			if ((this.left == null) && (this.right == null)) {
				return tfList.tokenize(line, fromIndex);
			}
			else if (this.right == null) {
				TFSeqStatic tf = new TFSeqStatic(this.left, tfList);
				tf.setRequiredFlags(new boolean[]{true, true});
				return tf.tokenize(line, fromIndex);
			}
			else if (this.left == null) {
				TFSeqStatic tf = new TFSeqStatic(tfList, this.right);
				tf.setRequiredFlags(new boolean[]{true, true});
				return tf.tokenize(line, fromIndex);
			} else {
				TFSeqStatic tf = new TFSeqStatic(this.left, tfList, this.right);
				tf.setRequiredFlags(new boolean[]{true, !this.allowNone, true});
				return tf.tokenize(line, fromIndex);				
			}			
		}
		return null;
	}
}
