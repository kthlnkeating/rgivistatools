package com.raygroupintl.vista.tools.returntype;

import com.raygroupintl.m.parsetree.data.EntryId;

public class ReturnTypeForFanout {

	private EntryId entryId;
	private ReturnTypeENUM returnTypeENUM;

	public EntryId getEntryId() {
		return entryId;
	}

	public void setEntryId(EntryId entryId) {
		this.entryId = entryId;
	}

	public ReturnTypeENUM getReturnTypeENUM() {
		return returnTypeENUM;
	}

	public void setReturnTypeENUM(ReturnTypeENUM returnTypeENUM) {
		this.returnTypeENUM = returnTypeENUM;
	}

	public ReturnTypeForFanout(EntryId entryId, ReturnTypeENUM returnTypeENUM) {
		super();
		this.entryId = entryId;
		this.returnTypeENUM = returnTypeENUM;
	}

	public String toString() {
		return entryId.toString2() + " - " +returnTypeENUM;
	}

}
