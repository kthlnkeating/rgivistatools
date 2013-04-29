package com.raygroupintl.vista.tools.returntype;

import com.raygroupintl.m.parsetree.data.BlockData;
import com.raygroupintl.m.parsetree.data.EntryId;

public class ReturnType extends BlockData {
	
	//basically just a prototype wrapper since ENUM's are singleton.
	private ReturnTypeENUM returnType;
	
	public ReturnType(EntryId entryId, ReturnTypeENUM returnType) {
		super(entryId);
		this.returnType = returnType;
	}
	
	public ReturnType(EntryId entryId) {
		super(entryId);
	}

	public ReturnTypeENUM getReturnType() {
		return returnType;
	}

	public void setReturnType(ReturnTypeENUM returnType) {
		this.returnType = returnType;
	}

}
