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

package com.raygroupintl.m.tool.routine.fanout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.tool.RoutineEntryLinks;

public class RoutineEntryLinksCollection {
	private Map<String, RoutineEntryLinks> map = new HashMap<String, RoutineEntryLinks>();

	public void put(String routineName, RoutineEntryLinks links) {
		this.map.put(routineName, links);
	}
	
	public Set<String> getRoutineNames() {
		return this.map.keySet();
	}
	
	public RoutineEntryLinks getRoutineEntryLinks(String routineName) {
		return this.map.get(routineName);
	}
	
	public Set<EntryId> addOrGetLinkedEntries(String routineName, String label) {
		RoutineEntryLinks links = this.map.get(routineName);
		if (links == null) {
			links = new RoutineEntryLinks();
			this.put(routineName, links);
		}
		return links.addOrGetLinkedEntries(label);		
	}
	
	public void addLink(EntryId keyEntryId, EntryId linkedEntryId){
		String routineName = keyEntryId.getRoutineName();
		String label = keyEntryId.getLabelOrDefault();
		Set<EntryId> linkedEntries = this.addOrGetLinkedEntries(routineName, label);
		linkedEntries.add(linkedEntryId);
	}
	
	public List<EntryId> getEmptyEntries() {
		List<EntryId> result = new ArrayList<EntryId>();
		Set<String> routineNames = this.map.keySet();
		for (String routineName : routineNames) {
			RoutineEntryLinks links = this.map.get(routineName);
			List<String> emptyLabels = links.getEmptyEntries();
			for (String emptyLabel : emptyLabels) {
				result.add(new EntryId(routineName, emptyLabel));
			}
		}
		return result;
	}
}
