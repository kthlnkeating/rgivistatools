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

package com.raygroupintl.m.tool.routine.fanin;

import java.util.List;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.tool.RoutineEntryLinks;
import com.raygroupintl.m.tool.routine.MRoutineToolInput;
import com.raygroupintl.m.tool.routine.RoutineToolParams;
import com.raygroupintl.m.tool.routine.fanout.FanoutTool;
import com.raygroupintl.m.tool.routine.fanout.RoutineEntryLinksCollection;

public class FaninTool {
	private FanoutTool fanoutTool;
	
	public FaninTool(RoutineToolParams params) {
		this.fanoutTool = new FanoutTool(params);
	}

	public RoutineEntryLinksCollection getResult(MRoutineToolInput input) {
		RoutineEntryLinksCollection fanoutLinks = this.fanoutTool.getResult(input);
		RoutineEntryLinksCollection finLinks = new RoutineEntryLinksCollection();
		Set<String> routineNames = fanoutLinks.getRoutineNames();
		for (String routineName : routineNames) {
			RoutineEntryLinks rel = fanoutLinks.getRoutineEntryLinks(routineName);
			Set<String> routineLabels = rel.getRoutineEntryLabels();
			for (String label : routineLabels) {
				finLinks.addOrGetLinkedEntries(routineName, label);				
				Set<EntryId> fanouts = rel.getLinkedEntries(label);
				for (EntryId id : fanouts) {
					finLinks.addLink(id, new EntryId(routineName, label));	
				}
			}			
		}
		return finLinks;
	}
	
	public List<EntryId> getTopEntries(MRoutineToolInput input) {
		RoutineEntryLinksCollection allLinks = this.getResult(input);
		return allLinks.getEmptyEntries();
	}
}
