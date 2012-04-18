package com.raygroupintl.vista.mtoken;

import java.util.EnumMap;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFChoice;

public class MTFSupply {
	private MVersion version;
	
	private MTFSupply(MVersion version) {
		this.version = version;
	}
	
	private ITokenFactory exprAtom;
	
	public ITokenFactory getTFExprAtom() {
		if (exprAtom == null) {
			if (version == MVersion.CACHE) {
				exprAtom = TFChoice.getInstance(TFGlvn.getInstance(version), TFExprItem.getInstance(version), TFCacheClassMethod.getInstance()) ;
			} else {
				exprAtom = TFChoice.getInstance(TFGlvn.getInstance(version), TFExprItem.getInstance(version));
			}			
		}
		return exprAtom;
	}
	
	private static EnumMap<MVersion, MTFSupply> SUPPLIES = new EnumMap<MVersion, MTFSupply>(MVersion.class);

	public static MTFSupply getInstance(MVersion version) {
		MTFSupply r = SUPPLIES.get(version);
		if (r == null) {
			r = new MTFSupply(version);
			SUPPLIES.put(version, r);
		}
		return r;
	}	
}
