package com.raygroupintl.vista.struct;

public class MLineLocation {
	private String tag;
	private int offset;
	
	public MLineLocation(String tag, int offset) {
		this.tag = tag;
		this.offset = offset;
	}
	
	@Override
	public boolean equals(Object rhs) {
		if ((rhs != null) && (rhs instanceof MLineLocation)) {	
			MLineLocation r = (MLineLocation) rhs;
			return this.tag.equals(r.tag) && (this.offset == r.offset); 
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		String hashOrigin = this.tag == null ? "" : this.tag;
		String hashOffset = this.offset == 0 ? "" : String.valueOf(this.offset);
		String hashString = hashOffset + "^" + hashOrigin;
		int result = hashString.hashCode(); 
		return result;
	}
}
