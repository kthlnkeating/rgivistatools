package com.raygroupintl.m.struct;

public class LineLocation {
	private String tag;
	private int offset;
	
	public LineLocation(String tag, int offset) {
		this.tag = tag;
		this.offset = offset;
	}
	
	public String getTag() {
		return this.tag;
	}
	
	public int getOffset() {
		return this.offset;
	}
	
	@Override
	public boolean equals(Object rhs) {
		if ((rhs != null) && (rhs instanceof LineLocation)) {	
			LineLocation r = (LineLocation) rhs;
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
	
	@Override
	public String toString() {
		return this.tag + "+" + String.valueOf(this.offset);
	}
}
