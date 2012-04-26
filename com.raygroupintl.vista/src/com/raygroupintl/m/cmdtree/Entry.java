package com.raygroupintl.m.cmdtree;

public class Entry extends Block<Line> {
	private String tagName;
	private String[] parameters;
	
	public Entry(String tagName) {
		this.tagName = tagName;
	}
	
	public Entry(String tagName, String[] parameters) {
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
}
