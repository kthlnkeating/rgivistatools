package com.raygroupintl.bnf;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public class TFChoiceBasic implements ITokenFactory {
	private ITokenFactory[] factories = {};
	
	public TFChoiceBasic() {
	}
	
	public TFChoiceBasic(ITokenFactory... factories) {
		this.factories = factories;
	}
	
	public void setFactories(ITokenFactory... factories) {
		this.factories = factories;
	}
	
	public int getNumChoices() {
		return this.factories.length;
	}
	
	@Override
	public IToken tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			for (ITokenFactory f : this.factories) {
				IToken result = f.tokenize(line, fromIndex);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}
	
	public boolean isInitialize() {
		return this.factories.length > 0;
	}
}
