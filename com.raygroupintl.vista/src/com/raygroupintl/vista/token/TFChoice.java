package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public abstract class TFChoice implements ITokenFactory {
	private static class Basic extends TFChoice {
		private ITokenFactory[] factories;
		
		public Basic(ITokenFactory[] factories) {
			this.factories = factories;
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
	}
 		
	public static TFChoice getInstance(ITokenFactory... fs) {
		return new Basic(fs);
	}
}
