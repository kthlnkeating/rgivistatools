//---------------------------------------------------------------------------
// Copyright 2012 Ray Group International
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//---------------------------------------------------------------------------

package com.raygroupintl.output;

import java.util.Arrays;

public class TerminalFormatter {
	private String eol = Utility.getEOL();
	
	private int tab = 0;
	private int width = 79;
	private int column = 0;
	private int listIndex = 0;
	
	public void setTab(int tab) {
		this.tab = tab;
	}
	
	private static String getSpaces(int count) {
		char[] spaces = new char[count];
		Arrays.fill(spaces, ' ');
		return new String(spaces);		
	}
	
	private String getTitle(String title) {
		int length = title.length();
		if (this.tab + 2 < length) {
			this.column = length;
			return title;
		} else {
			String result = TerminalFormatter.getSpaces(this.tab - length - 2);
			result += title + ':' + ' ';
			this.column = result.length();
			return result;
		}
		
	}
	
	public String startList(String title) {
		this.listIndex = 0;
		return this.getTitle(title);
	}
	
	public String titled(String title, String msg) {
		String result = this.getTitle(title);
		result += msg;
		return result;
	}
	
	public String addToList(String listElement) {
		int length = listElement.length();
		if (this.listIndex == 0) {
			this.column += length;
			++this.listIndex;
			return listElement;
		}		
		if (this.column + length + 1 < this.width) {
			this.column += length + 1;
			++this.listIndex;
			return ',' + listElement;
		}
		String result = this.eol + TerminalFormatter.getSpaces(this.tab);
		result += listElement;
		this.column = length + this.tab;
		this.listIndex = 0;		
		return result;
	}
}
