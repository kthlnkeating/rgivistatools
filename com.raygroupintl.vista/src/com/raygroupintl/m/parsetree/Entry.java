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

public class Entry extends NodeList<Node> {
	private static final long serialVersionUID = 1L;

	private String name;
	private String routineName;
	private int index;
	private String[] parameters;
	
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
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visitEntry(this);
	}
}
