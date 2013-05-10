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

package com.raygroupintl.m.tool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.EntryId;

public class RoutineEntryLinks implements Serializable {
	private static final long serialVersionUID = 1L;

	private Map<String, Set<EntryId>> linked = new HashMap<String, Set<EntryId>>();
	
	public Set<EntryId> put(String entryLabel, Set<EntryId> linkedEntries) {
		return this.linked.put(entryLabel, linkedEntries);
	}
	
	public Set<String> getRoutineEntryLabels() {
		return this.linked.keySet();
	}
	
	public Set<EntryId> getLinkedEntries(String entryLabel) {
		return this.linked.get(entryLabel);
	}
	
	public Set<EntryId> addOrGetLinkedEntries(String label) {
		Set<EntryId> linkedEntries = this.linked.get(label);
		if (linkedEntries == null) {
			linkedEntries = new HashSet<EntryId>();
			this.linked.put(label, linkedEntries);
		}
		return linkedEntries;
	}
	
	public List<String> getEmptyEntries() {
		List<String> result = new ArrayList<String>();
		Set<String> labels = this.linked.keySet();
		for (String label : labels) {
			Set<EntryId> linkedForEntry = this.linked.get(label);
			if (linkedForEntry.size() == 0) {
				result.add(label);
			}
		}
		return result;
	}
}
