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

package com.raygroupintl.m.tool.routine.occurance;

import java.util.EnumSet;
import java.util.List;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.routine.MRoutineToolInput;

public class OccuranceTool {
	private ParseTreeSupply pts;
	private EnumSet<OccuranceType> types;
	
	public OccuranceTool(OccuranceToolParams params) {
		this.pts = params.getParseTreeSupply();
		this.types = params.getIncludeTypes();
	}
	
	public OccurancesByRoutine getResult(MRoutineToolInput input) {
		OccurancesByRoutine result = new OccurancesByRoutine();
		List<String> routineNames = input.getRoutineNames(); 		
		OccuranceRecorder fr = new OccuranceRecorder();
		fr.setRequestedTypes(this.types);
		for (String routineName : routineNames) {
			Routine routine = this.pts.getParseTree(routineName);
			OccurancesByLabel rfs = fr.getOccurances(routine);
			result.put(routineName, rfs);
		}
		return result;
	}

}
