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

import com.raygroupintl.m.parsetree.data.EntryId;

public class Entry extends NodeList<Line> {
	private static final long serialVersionUID = 1L;

	private String name;
	private String routineName;
	private int index;
	private String[] parameters;
	
	private int endIndex = -1;
	private Entry continuationEntry;
	
	public Entry(String name, String routineName, int index, String[] parameters) {
		this.name = name;
		this.routineName = routineName;
		this.index = index;
		this.parameters = parameters;
	}
	
	public String getKey() {
		return this.name + '^' + this.routineName + ',' + String.valueOf(this.index);
	}
	
	public String getName() {
		return this.name;
	}
	
	public String[] getParameters() {
		return this.parameters;
	}

	public EntryId getFullEntryId() {
		if ((this.name == null) || this.name.isEmpty()) {
			return new EntryId(this.routineName, this.routineName);
		} else {
			return new EntryId(this.routineName, this.name);
		}
		
	}
	
	public int getEndIndex() {
		return this.endIndex;
	}
	
	public boolean isClosed() {
		return this.endIndex > -1;
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visitEntry(this);
	}

	@Override
	public void add(Line node) {
		if (this.endIndex >= 0) {
			node.tranformToClosed();
		} else if (node.isClosed()) {
			this.endIndex = this.size();
		}
		super.add(node);
	}
	
	public void setContinuationEntry(Entry entry) {
		this.continuationEntry = entry;
	}

	public Entry getContinuationEntry() {
		return this.continuationEntry;
	}
}
