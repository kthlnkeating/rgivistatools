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

package com.pwc.us.rgi.m.tool.routine.fanout;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pwc.us.rgi.m.parsetree.Routine;
import com.pwc.us.rgi.m.parsetree.data.EntryId;
import com.pwc.us.rgi.m.tool.EntryIdsByLabel;
import com.pwc.us.rgi.m.tool.EntryIdsByRoutine;
import com.pwc.us.rgi.m.tool.ParseTreeSupply;
import com.pwc.us.rgi.m.tool.routine.MRoutineToolInput;
import com.pwc.us.rgi.m.tool.routine.ResultRoutineSpace;
import com.pwc.us.rgi.m.tool.routine.RoutineToolParams;
import com.pwc.us.rgi.struct.Filter;

public class FanoutTool {	
	private static class RoutineNameFilter implements Filter<EntryId> {
		private Set<String> routineNames;
		
		public RoutineNameFilter(Set<String> routineNames) {
			this.routineNames = routineNames;
		}
		
		public RoutineNameFilter(Collection<String> routineNames) {
			this(new HashSet<String>(routineNames));
		}
		
		@Override
		public boolean isValid(EntryId id) {
			String routineName = id.getRoutineName();
			if (routineName != null) {
				return this.routineNames.contains(routineName);
			} else {
				return true;
			}
		}
	}
	
	private ParseTreeSupply pts;
	private ResultRoutineSpace resultRoutineSpace;
	
	public FanoutTool(RoutineToolParams params) {
		this.pts = params.getParseTreeSupply();
		this.resultRoutineSpace = params.getResultRoutineSpace();
	}
	
	public EntryIdsByRoutine getResult(MRoutineToolInput input) {
		EntryIdsByRoutine result = new EntryIdsByRoutine();
		List<String> routineNames = input.getRoutineNames(); 		
		FanoutRecorder fr = new FanoutRecorder();
		if (resultRoutineSpace == ResultRoutineSpace.ALL_INPUT_ROUTINES) {
			Filter<EntryId> filter = new RoutineNameFilter(routineNames);
			fr.setFilter(filter);
		}
		for (String routineName : routineNames) {
			Routine routine = this.pts.getParseTree(routineName);
			EntryIdsByLabel rfs = fr.getFanouts(routine);
			result.put(routineName, rfs);
		}
		return result;
	}
}
