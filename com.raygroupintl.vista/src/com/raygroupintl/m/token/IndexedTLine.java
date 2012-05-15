package com.raygroupintl.m.token;

class IndexedTLine {
	private TLine line;
	private String tagName;
	private int tagOffset;

	public IndexedTLine(TLine line, String tagName, int tagOffset) {
		this.line = line;
		this.tagName = tagName;
		this.tagOffset = tagOffset;
	}

	public TLine getTLine() {
		return this.line;
	}
	
	public String getTagName() {
		return this.tagName;
	}
	
	public int getTagOffSet() {
		return this.tagOffset;
	}
}
