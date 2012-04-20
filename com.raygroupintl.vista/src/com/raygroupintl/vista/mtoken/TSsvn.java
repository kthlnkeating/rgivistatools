package com.raygroupintl.vista.mtoken;

import java.util.Arrays;
import java.util.List;

import com.raygroupintl.bnf.TArray;
import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.struct.MNameWithMnemonic;

public class TSsvn extends TArray {
	private static final MNameWithMnemonic.Map SSVS = new MNameWithMnemonic.Map();
	static {
		SSVS.update("D", "DEVICE"); 	
		SSVS.update("DI", "DISPLAY"); 	
		SSVS.update("E", "EVENT"); 	
		SSVS.update("G", "GLOBAL"); 	
		SSVS.update("J", "JOB"); 	
		SSVS.update("L", "LOCK"); 	
		SSVS.update("R", "ROUTINE"); 	
		SSVS.update("S", "SYSTEM"); 	
		SSVS.update("W", "WINDOW"); 	
	}
	
	private static class TSsvnName  extends TKeyword {
		public TSsvnName(String value) {
			super(value);
		}
		
		@Override
		public String getStringValue() {
			return "^$" + super.getStringValue();
		}

		@Override
		public int getStringSize() {
			return 2 + super.getStringSize();
		}	

		@Override
		public List<MError> getErrors() {
			String value = this.getIdentier();
			if (SSVS.get(value) == null) {
				return Arrays.asList(new MError[]{new MError(MError.ERR_UNKNOWN_INTRINSIC_FUNCTION)});
			} else {
				return null;
			}
		}
		
		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			String value = this.getIdentier();
			MNameWithMnemonic name = SSVS.get(value);
			return name;
		}		
	}

	private TSsvn(TSsvnName name, IToken param) {
		super(new IToken[]{name, param});
	}
	
	public static TSsvn getInstance(TIdent name, IToken param) {
		String v = name.getStringValue();
		return new TSsvn(new TSsvnName(v), param);
	}
}
