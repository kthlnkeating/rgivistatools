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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeList<T extends Node> extends Nodes<T> {
	private List<T> nodes = new ArrayList<T>();
	public static int allocated;
	public static int added;
	
	public NodeList() {		
	}
	
	public NodeList(int size) {
		this.nodes = new ArrayList<T>(size);	
		allocated += size;
	}

	public void reset(int size) {
		this.nodes = new ArrayList<T>(size);
		allocated += size;
	}
	
	public void add(T node) {
		if (this.nodes == null) {
			this.nodes = new ArrayList<T>();
			allocated += 10;
		}
		added += 1;
		this.nodes.add(node);
	}
	
	public T getLastNode() {
		if (this.nodes != null) {
			int lastIndex = this.nodes.size() - 1;
			if (lastIndex > 0) {
				return this.nodes.get(lastIndex);
			}
		}
		return null;
	}
		
	@Override
	public List<T> getNodes() {
		if (this.nodes == null) {
			return Collections.emptyList();
		} else {
			return Collections.unmodifiableList(this.nodes);
		}
	}
	
	@Override
	public boolean setEntryList(EntryList entryList) {
		return false;
	}	
}
