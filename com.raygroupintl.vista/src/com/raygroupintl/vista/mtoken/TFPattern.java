package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialOR;
import com.raygroupintl.vista.token.TSyntaxError;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFAllOptional;
	
public class TFPattern extends TFParallelCharBased {
	static abstract class TFPatOnSomething extends TFCharAccumulating {
		@Override
		protected IToken getToken(String line, int fromIndex) {
			return new TSyntaxError(line, line.length(), fromIndex);			
		}

		@Override
		protected boolean stopOn(char ch) {
			return this.isRightStop(ch) || ! Library.isIdent(ch);
		}
				
		@Override
		protected IToken getToken(String line, int fromIndex, int endIndex) {
			String value = line.substring(fromIndex, endIndex+1);
			return new TBasic(value);
		}
	}
		
	static class TFPatOnYy extends TFPatOnSomething {
		@Override
		protected boolean isRightStop(char ch) {
			return (ch == 'y') || (ch == 'Y');
		}
	}
	
	static class TFPatOnZz extends TFPatOnSomething {
		@Override
		protected boolean isRightStop(char ch) {
			return (ch == 'z') || (ch == 'Z');
		}
	}
	
	static class TFPatOn extends TFCharAccumulating {
		@Override
		protected IToken getToken(String line, int fromIndex) {
			String value = line.substring(fromIndex);
			return new TBasic(value);			
		}

		@Override
		protected boolean stopOn(char ch) {
			return ! ((ch >= 'a') && (ch < 'y')) || ((ch >= 'A') && (ch <'Y'));
		}
				
		@Override
		protected IToken getToken(String line, int fromIndex, int endIndex) {
			String value = line.substring(fromIndex, endIndex);
			return new TBasic(value);
		}
		
		@Override
		protected boolean isRightStop(char ch) {
			return true;
		}
	}
	
	static class TFPatOns extends TFParallelCharBased {
		@Override
		protected ITokenFactory getFactory(char ch) {
			if (((ch >= 'a') && (ch < 'y')) || ((ch >= 'A') && (ch <'Y'))) {
				return new TFPatOn();
			}
			if ((ch == 'Y') || (ch == 'y')) {
				return new TFPatOnYy();
			}
			if ((ch == 'Z') || (ch == 'z')) {
				return new TFPatOnZz();
			}
			return null;
		}
	}
	
	static class TFPatCode extends TFSerialOR {
		@Override
		protected ITokenFactory getRequired() {
			return new TFPatOns();
		}
		
		@Override
		protected ITokenFactory getOptional() {
			return new TFConstChar('\'');
		}
	}
	
	static class TFRepCount extends TFAllOptional {
		@Override
		protected ITokenFactory[] getFactories() {
			ITokenFactory f = new TIntLit.Factory();
			ITokenFactory c = new TFConstChar('.');
			return new ITokenFactory[]{f, c, f};
		}
	}
	
	static class TFPatAtom extends TFAllRequired {
		@Override
		protected ITokenFactory[] getFactories() {
			ITokenFactory r = new TFRepCount();
			ITokenFactory c = new TFParallelCharBased() {
				@Override
				protected ITokenFactory getFactory(char ch) {
					if (ch == '(') return new TFAlternation();
					if (Library.isIdent(ch)) return new TFPatCode();
					if (Library.isDigit(ch)) return new TIntLit.Factory();
					return null;
				}
			};
			return new ITokenFactory[]{r, c};
		}
	}
	
	static class TFAlternation extends TFInParantheses {
		@Override
		protected ITokenFactory getInnerfactory() {
			return new TFDelimitedList() {
				@Override
				protected ITokenFactory getElementFactory() {
					return new TFPatAtom();
				}
				
				@Override
				protected ITokenFactory getDelimitedFactory() {
					return new TFConstChar(',');
				}
			};
		}
	}	
	
	@Override
	protected ITokenFactory getFactory(char ch) {
		if (ch == '@') {
			return new TFIndirection();
		} else {
			return new TFPatAtom();
		}
	}
	
	public static IToken run(String line, int fromIndex) {
		ITokenFactory f = new TFPattern();
		return f.tokenize(line, fromIndex);
	}

	public static TFPattern getInstance() {
		return new TFPattern();
	}
}
