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

package com.raygroupintl.m.parsetree;

public class Line extends NodeList<Node> {
	private String tag;
	private int index;
	private int level;
		
	public Line(String tag, int index, int level) {
		this.tag = tag;
		this.index = index;
		this.level = level;
	}

	public String getTag() {
		return this.tag;
	}
	
	public int getIndex() {
		return this.index;
	}

	public int getLevel() {
		return this.level;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitLine(this);
	}
	
	@Override
	public boolean setEntryList(EntryList entryList) {
		boolean result = false;
		for (Node node : this.getNodes()) {
			boolean nodeResult = node.setEntryList(entryList);
			result = result || nodeResult; 					
		}
		return result;
	}	
}
