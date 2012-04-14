package com.raygroupintl.vista.mtoken;

import java.util.List;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenArray;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFSerialROO;
import com.raygroupintl.vista.token.TFSerialRRO;
import com.raygroupintl.vista.token.TPair;
import com.raygroupintl.vista.token.TBase;

public class External {
	public static class TReference extends TBase {
		private String routineName;
		private String packageName;
		private String label;
		
		public TReference(String routineName, String packageName, String label) {
			this.routineName = routineName;
			this.packageName = packageName;
			this.label = label;
		}
		
		public TReference(TReference r) {
			this(r.routineName, r.packageName, r.label);
		}
		
		@Override
		public String getStringValue() {
			String result = "&";
			if (this.packageName != null) {
				result += this.packageName + ".";
			}
			if (this.label != null) {
				result += this.label + '^';
			}
			result += this.routineName;
			return result;
		}

		@Override
		public List<MError> getErrors() {
			return null;
		}

		@Override
		public void beautify() {
		}
	}
	
	public static class TReferenceWithArgument extends TPair {
		public TReferenceWithArgument(TReference reference, TActualList actualList) {
			super(reference, actualList);
		}

		public TReferenceWithArgument(TPair p) {
			super(p);
		}		
	}
	
	public static class TVariable extends TReference {
		public TVariable(TReference r) {
			super(r);
		}

		@Override
		public String getStringValue() {
			return '$' + super.getStringValue();
		}
	
		@Override
		public int getStringSize() {
			return 1 + super.getStringSize();
		}
	}
	
	public static class TFunction extends TReferenceWithArgument {
		public TFunction(TPair p) {
			super(p);
		}

		@Override
		public String getStringValue() {
			return '$' + super.getStringValue();
		}
	
		@Override
		public int getStringSize() {
			return 1 + super.getStringSize();
		}
	}
	
	private static final class TFAmpersandTail extends TFSerialROO {
		@Override
		protected final ITokenFactory[] getFactories() {
			TFName n = new TFName();
			TFAllRequired p = TFAllRequired.getInstance(new TFConstChar('.'), n);
			TFAllRequired r = TFAllRequired.getInstance(new TFConstChar('^'), n);
			return new ITokenFactory[]{n, p, r};
		}
		
		@Override
		protected final IToken getTokenWhenNoOptional(IToken name) {
			return new TReference(name.getStringValue(), null, null);
		}
		
		protected final IToken getTokenWhenAnyOptional(IToken[] foundTokens) {
			if (foundTokens[1] == null) {
				String label = foundTokens[0].getStringValue();
				String routine = ((ITokenArray) foundTokens[2]).get(1).getStringValue();
				return new TReference(routine, null, label);
			}
			else if (foundTokens[2] == null) {
				String packageName = foundTokens[0].getStringValue();
				String routine = ((ITokenArray) foundTokens[1]).get(1).getStringValue();
				return new TReference(routine, packageName, null);			
			} else {
				String packageName = foundTokens[0].getStringValue();
				String label = ((ITokenArray) foundTokens[1]).get(1).getStringValue();
				String routine = ((ITokenArray) foundTokens[2]).get(1).getStringValue();
				return new TReference(routine, packageName, label);								
			}
		}
	}
			
	public static final class TF extends TFSerialRRO {
		private MVersion version;
		
		protected TF(MVersion version) {		
			this.version = version;
		}
			
		@Override
		protected final ITokenFactory[] getFactories() {
			return new ITokenFactory[]{new TFConstChar('&'), new TFAmpersandTail(), TFActualList.getInstance(this.version)};
		}

		@Override
		protected final IToken getToken(IToken[] foundTokens) {
			if (foundTokens[2] == null) {
				return foundTokens[1];
			} else {
				return new TReferenceWithArgument((TReference) foundTokens[1], (TActualList) foundTokens[2]);
			}
		}

		//@Override
		//protected final IToken getTokenWhenNoOptional(IToken[] foundTokens) {
		//	return foundTokens[1];
		//}
		//
		//@Override
		//protected final IToken getTokenWhenAll(IToken[] foundTokens) {
		//	return new TReferenceWithArgument((TReference) foundTokens[1], (TActualList) foundTokens[2]);			
		//}
	}
	
	public static boolean isTFCreated(IToken token) {
		return (token instanceof TReference) || (token instanceof TReferenceWithArgument);
	}
}
