package com.raygroupintl.m.cmdtree;

public class EntryTag extends Block<Line> {
	private String tagName;
	private String[] parameters;
	
	public EntryTag(String tagName) {
		this.tagName = tagName;
	}
	
	public EntryTag(String tagName, String[] parameters) {
		this.parameters = parameters;
	}
		
	public String getKey() {
		return this.tagName;
	}
	
	public int getParameterCount() {
		return this.parameters.length;
	}
	
	public String getParameter(int index) {
		return this.parameters[index];
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitEntryTag(this);
	}
}
