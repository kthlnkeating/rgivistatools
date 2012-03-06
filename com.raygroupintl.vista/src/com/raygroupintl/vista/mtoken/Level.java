package com.raygroupintl.vista.mtoken;


public class Level extends Basic {
	public Level(String value) {
		super(value);
	}
	
	public static Level getInstance(String line, int fromIndex) {
		int toIndex = Algorithm.findOther(line, fromIndex, ' ', '.');
		if (toIndex == fromIndex) {
			return new Level("");
		} else {
			return new Level(line.substring(fromIndex, toIndex));
		}
	}

}
