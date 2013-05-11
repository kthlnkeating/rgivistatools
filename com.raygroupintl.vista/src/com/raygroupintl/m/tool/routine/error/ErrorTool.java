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

package com.raygroupintl.m.tool.routine.error;

import java.util.List;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.routine.MRoutineToolInput;
import com.raygroupintl.m.tool.routine.RoutineToolParams;

public class ErrorTool {
	private ParseTreeSupply pts;
	
	public ErrorTool(RoutineToolParams params) {
		this.pts = params.getParseTreeSupply();
	}
	
	public ErrorsByRoutine getResult(MRoutineToolInput input) {
		ErrorsByRoutine result = new ErrorsByRoutine();
		List<String> routineNames = input.getRoutineNames(); 		
		ErrorRecorder fr = new ErrorRecorder();
		for (String routineName : routineNames) {
			Routine routine = this.pts.getParseTree(routineName);
			ErrorsByLabel rfs = fr.getErrors(routine);
			result.put(routineName, rfs);
		}
		return result;
	}
}
