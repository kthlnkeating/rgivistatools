package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.token.TBasic;


public class Level extends TBasic {
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
