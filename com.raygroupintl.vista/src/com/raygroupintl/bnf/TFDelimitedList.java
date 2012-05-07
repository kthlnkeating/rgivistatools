package com.raygroupintl.bnf;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public final class TFDelimitedList implements ITokenFactory {
	private static class DLAdapter implements TokenAdapter {
		@Override
		public IToken convert(String line, int fromIndex, IToken[] tokens) {
			if (tokens[1] == null) {
				return new TList(tokens[0]);	
			} else {		
				TList result = (TList) tokens[1];
				result.add(0, tokens[0]);
				return result;
			}			
		}
	}
		
	private ITokenFactory elementFactory;
	private ITokenFactory delimiter;
	private ITokenFactory left;
	private ITokenFactory right;
	private boolean allowEmpty;
	private boolean allowNone;
	
	public TFDelimitedList() {		
	}
	
	public TFDelimitedList(ITokenFactory elementFactory) {	
		this.elementFactory = elementFactory;
	}

	public TFDelimitedList(ITokenFactory elementFactory, char ch) {	
		this.elementFactory = elementFactory;
		this.delimiter = new TFConstChar(ch);
	}

	public void setElementFactory(ITokenFactory elementFactory) {
		this.elementFactory = elementFactory;
	}
	
	private ITokenFactory getStringFactory(String value) {
		if (value.length() == 1) {
			char ch = value.charAt(0);
			return new TFConstChar(ch);
		} else {
			return new TFConstString(value);
		}
	}
	
	public void setDelimiter(ITokenFactory delimeter) {
		this.delimiter = delimeter;
	}

	public void setDelimiter(String delimeter) {
		ITokenFactory tf = this.getStringFactory(delimeter);
		this.setDelimiter(tf);
	}
	
	public void setLeft(ITokenFactory left) {
		this.left = left;
	}

	public void setLeft(String left) {
		ITokenFactory tf = this.getStringFactory(left);
		this.setLeft(tf);
	}
	
	public void setRight(ITokenFactory right) {
		this.right = right;
	}

	public void setRight(String right) {
		ITokenFactory tf = this.getStringFactory(right);
		this.setRight(tf);
	}
	
	public void setAllowEmpty(boolean b) {
		this.allowEmpty = b;
	}
	
	public void setAllowNone(boolean b) {
		this.allowNone = b;
	}
	
	private ITokenFactory getEffectiveListTailElementFactory() {
		if (this.allowEmpty) {
			ITokenFactory eDelimiter = new TFEmpty(this.delimiter);
			if (this.right == null) {
				return new TFChoiceBasic(this.elementFactory, eDelimiter);
			} else {
				ITokenFactory eRight = new TFEmpty(this.right);
				return new TFChoiceBasic(this.elementFactory, eDelimiter, eRight);
			}
		} else {
			return this.elementFactory;
		}
	}
	
	private ITokenFactory getLeadingElement() {
		if (this.allowEmpty) {
			ITokenFactory eDelimiter = new TFEmpty(this.delimiter);
			return new TFChoiceBasic(this.elementFactory, eDelimiter);
		} else {
			return this.elementFactory;
		}
		
	}
	
	private ITokenFactory getEffectiveListFactory() {
		if (this.delimiter == null) {
			return new TFList(this.elementFactory);
		} else {
			ITokenFactory tailElement = this.getEffectiveListTailElementFactory();
			TFSeqStatic tailSingle = new TFSeqStatic(this.delimiter, tailElement);
			tailSingle.setRequiredFlags(new boolean[]{true, true});
			ITokenFactory tail = new TFList(tailSingle);
			ITokenFactory leadingElement = this.getLeadingElement();
			TFSeqStatic result = new TFSeqStatic(leadingElement, tail);
			result.setRequiredFlags(new boolean[]{true, false});
			result.setTokenAdapter(new DLAdapter());				
			return result;
		}
	}
	
	@Override
	public IToken tokenize(String line, int fromIndex) {
		if (line.length() > fromIndex) {
			ITokenFactory tfList = this.getEffectiveListFactory();
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
