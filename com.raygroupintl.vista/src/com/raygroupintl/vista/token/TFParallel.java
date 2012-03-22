package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public abstract class TFParallel implements ITokenFactory {
	protected abstract ITokenFactory[] getFactories();
	
	@Override
	public IToken tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			ITokenFactory[] fs = this.getFactories();
			for (ITokenFactory f : fs) {
				IToken result = f.tokenize(line, fromIndex);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}
	
	public static TFParallel getInstance(final ITokenFactory f0, final ITokenFactory f1) {
		return new TFParallel() {			
			@Override
			protected ITokenFactory[] getFactories() {
				return new ITokenFactory[]{f0, f1};
			}
		};
	}
}
