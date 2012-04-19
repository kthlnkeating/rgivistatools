package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllOptional;
import com.raygroupintl.vista.token.TFBasic;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFConstChars;
import com.raygroupintl.vista.token.TFChoice;
import com.raygroupintl.vista.token.TFSerialOR;
import com.raygroupintl.vista.token.TFAllRequired;
	
public class TFPattern extends TFChoice {
	private MVersion version;
	
	private TFPattern(MVersion version) {
		this.version = version;
	}
		
	static class TFPatOnYy extends  TFAllRequired {
		@Override
		protected ITokenFactory[] getFactories() {
			ITokenFactory y = TFConstChars.getInstance("Zz");
			ITokenFactory m = new TFBasic() {				
				@Override
				protected boolean stopOn(char ch) {
					return (ch == 'Y') || (ch == 'y') || ! Library.isIdent(ch);
				}
			};
			return new ITokenFactory[]{y, m, y};
		}
	}
	
	static class TFPatOnZz extends TFAllRequired {
		@Override
		protected ITokenFactory[] getFactories() {
			ITokenFactory z = TFConstChars.getInstance("Zz");
			ITokenFactory m = new TFBasic() {				
				@Override
				protected boolean stopOn(char ch) {
					return (ch == 'Z') || (ch == 'z') || ! Library.isIdent(ch);
				}
			};
			return new ITokenFactory[]{z, m, z};
		}
	}
	
	static class TFPatOn extends TFBasic {
		@Override
		protected boolean stopOn(char ch) {
			return ! (((ch >= 'a') && (ch < 'y')) || ((ch >= 'A') && (ch <'Y')));
		}
	}
	
	static class TFPatOns extends TFChoice {
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
			ITokenFactory c = new TFChoice() {
				@Override
				protected ITokenFactory getFactory(char ch) {
					if (ch == '(') return new TFAlternation();
					if (Library.isIdent(ch)) return new TFPatCode();
					if (ch == '"') return TFStringLiteral.getInstance();
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
					return TFList.getInstance(new TFPatAtom());
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
			return TFIndirection.getInstance(this.version);
		} else {
			return TFList.getInstance(new TFPatAtom());
		}
	}
	
	//public static IToken run(String line, int fromIndex) {
	//	ITokenFactory f = new TFPattern();
	//	return f.tokenize(line, fromIndex);
	//}

	public static TFPattern getInstance(MVersion version) {
		return new TFPattern(version);
	}
}
