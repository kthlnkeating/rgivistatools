package com.raygroupintl.m.token;

import java.util.Arrays;
import java.util.List;

import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TArray;
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
			return "^$" + this.getIdentier();
		}

		@Override
		public int getStringSize() {
			return 2 + this.getIdentier().length();
		}	

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			String value = this.getIdentier();
			MNameWithMnemonic name = SSVS.get(value);
			return name;
		}		
	}

	private TSsvn(TSsvnName name, Token param) {
		super(Arrays.asList(new Token[]{name, param}));
	}
}
