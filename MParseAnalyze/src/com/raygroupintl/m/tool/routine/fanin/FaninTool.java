//---------------------------------------------------------------------------
// Copyright 2013 PwC
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
import com.raygroupintl.m.tool.EntryIdsByRoutine;
import com.raygroupintl.m.tool.ResultsByLabel;
import com.raygroupintl.m.tool.routine.MRoutineToolInput;
import com.raygroupintl.m.tool.routine.RoutineToolParams;
import com.raygroupintl.m.tool.routine.fanout.FanoutTool;

public class FaninTool {
	private FanoutTool fanoutTool;
	
	public FaninTool(RoutineToolParams params) {
		this.fanoutTool = new FanoutTool(params);
	}

	public EntryIdsByRoutine getResult(MRoutineToolInput input) {
		EntryIdsByRoutine fanoutLinks = this.fanoutTool.getResult(input);
		EntryIdsByRoutine finLinks = new EntryIdsByRoutine();
		Set<String> routineNames = fanoutLinks.getRoutineNames();
		for (String routineName : routineNames) {
			ResultsByLabel<EntryId, Set<EntryId>> rel = fanoutLinks.getResults(routineName);
			Set<String> routineLabels = rel.getLabels();
			for (String label : routineLabels) {
				finLinks.getResultsAddingWhenNone(routineName, label);				
				Set<EntryId> fanouts = rel.getResults(label);
				for (EntryId id : fanouts) {
					finLinks.addResult(id, new EntryId(routineName, label));	
				}
			}			
		}
		return finLinks;
	}
	
	public List<EntryId> getTopEntries(MRoutineToolInput input) {
		EntryIdsByRoutine allLinks = this.getResult(input);
		return allLinks.getEmptyEntries();
	}
}
