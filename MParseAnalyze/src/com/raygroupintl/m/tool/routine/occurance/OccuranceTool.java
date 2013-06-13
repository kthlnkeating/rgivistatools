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

package com.raygroupintl.m.tool.routine.occurance;

import java.util.EnumSet;
import java.util.List;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.ResultsByLabel;
import com.raygroupintl.m.tool.ResultsByRoutine;
import com.raygroupintl.m.tool.routine.CollectionAsToolResult;
import com.raygroupintl.m.tool.routine.MRoutineToolInput;

public class OccuranceTool {
	private ParseTreeSupply pts;
	private EnumSet<OccuranceType> types;
	
	public OccuranceTool(OccuranceToolParams params) {
		this.pts = params.getParseTreeSupply();
		this.types = params.getIncludeTypes();
	}
	
	public CollectionAsToolResult<Occurance> getResult(EntryId entryUnderTest) {
		String routineName = entryUnderTest.getRoutineName();
		Routine routine = this.pts.getParseTree(routineName);
		OccuranceRecorder or = new OccuranceRecorder();
		or.setRequestedTypes(this.types);
		String tag = entryUnderTest.getLabelOrDefault();
		ResultsByLabel<Occurance, List<Occurance>> rfs = or.getResults(routine);
		List<Occurance> result = rfs.getResults(tag);
		return new CollectionAsToolResult<Occurance>(entryUnderTest, result);
	}
	
	public ResultsByRoutine<Occurance, List<Occurance>> getResult(List<String> routineNames) {
		OccurancesByRoutine result = new OccurancesByRoutine();
		OccuranceRecorder or = new OccuranceRecorder();
		or.setRequestedTypes(this.types);
		for (String routineName : routineNames) {
			Routine routine = this.pts.getParseTree(routineName);
			ResultsByLabel<Occurance, List<Occurance>> rfs = or.getResults(routine);
			result.put(routineName, rfs);
		}
		return result;
	}

	public ResultsByRoutine<Occurance, List<Occurance>> getResult(MRoutineToolInput input) {
		List<String> routineNames = input.getRoutineNames(); 
		return this.getResult(routineNames);
	}
}
