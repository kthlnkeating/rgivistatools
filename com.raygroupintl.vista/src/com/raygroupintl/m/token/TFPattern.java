package com.raygroupintl.m.token;

import com.raygroupintl.bnf.TFBasic;
import com.raygroupintl.bnf.TFChoice;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFConstChars;
import com.raygroupintl.bnf.TFDelimitedList;
import com.raygroupintl.bnf.TFList;
import com.raygroupintl.bnf.TFSeqOR;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.bnf.TFSeqStatic;
import com.raygroupintl.fnds.ITokenFactory;
	
public class TFPattern extends TFChoice {
	private MVersion version;
	
	private TFPattern(MVersion version) {
		this.version = version;
	}
		
	static class TFPatOnYy extends  TFSeqRequired {
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
	
	static class TFPatOnZz extends TFSeqRequired {
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
	
	static class TFPatCode extends TFSeqOR {
		@Override
		protected ITokenFactory[] getFactories() {
			return new ITokenFactory[]{new TFConstChar('\''), new TFPatOns()};
		}
	}
	
	static class TFRepCount extends TFSeqStatic {
		@Override
		protected ITokenFactory[] getFactories() {
			ITokenFactory f = new TIntLit.Factory();
			ITokenFactory c = new TFConstChar('.');
			return new ITokenFactory[]{f, c, f};
		}
	}
	
	static class TFPatAtom extends TFSeqRequired {
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
			return MTFSupply.getInstance(version).indirection;
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
