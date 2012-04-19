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
	
	private static class CharSelected extends TFChoice {
		private ITokenFactory defaultFactory;
		private String keys;
		private ITokenFactory[] factories;
				
		public CharSelected(ITokenFactory defaultFactory, String keys, ITokenFactory[] factories) {
			this.defaultFactory = defaultFactory;
			this.keys = keys;
			this.factories = factories;
		}

		protected ITokenFactory getFactory(char ch) {
			int factoryIndex = this.keys.indexOf(ch);
			if (factoryIndex < 0) {
				return this.defaultFactory;
			} else {
				return this.factories[factoryIndex];
			}						
		}
		
		protected ITokenFactory getFactory(String line, int index) {
			char ch = line.charAt(index);
			return this.getFactory(ch);
		}
		
		@Override
		public IToken tokenize(String line, int fromIndex) {
			int endIndex = line.length();
			if (fromIndex < endIndex) {
				ITokenFactory f = this.getFactory(line, fromIndex);
				if (f != null) {
					IToken result = f.tokenize(line, fromIndex);
					return result;
				}
			}
			return null;
		}		
	}
	
	private static class Char2ndSelected extends CharSelected {
		public Char2ndSelected(ITokenFactory defaultFactory, String keys, ITokenFactory[] factories) {
			super(defaultFactory, keys, factories);
		}

		@Override
		protected ITokenFactory getFactory(String line, int index) {
			if (index+1 < line.length()) {			
				char ch = line.charAt(index);
				return this.getFactory(ch);
			} else {
				return null;
			}
		}
		
	}

	public static TFChoice getInstance(ITokenFactory... factories) {
		return new Basic(factories);
	}

	public static TFChoice getInstance(ITokenFactory defaultFactory, String keys, ITokenFactory... factories) {
		return new CharSelected(defaultFactory, keys, factories);
	}

	public static TFChoice getInstance(ITokenFactory defaultFactory, char key, ITokenFactory factory) {
		return getInstance(defaultFactory, String.valueOf(key), factory);
	}

	public static TFChoice getInstanceFor2ndChar(ITokenFactory defaultFactory, String keys, ITokenFactory... factories) {
		return new Char2ndSelected(defaultFactory, keys, factories);
	}
}
