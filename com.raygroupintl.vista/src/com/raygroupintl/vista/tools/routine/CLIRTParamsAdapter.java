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

package com.raygroupintl.vista.tools.routine;

import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.routine.MRoutineToolInput;
import com.raygroupintl.m.tool.routine.RoutineToolParams;
import com.raygroupintl.vista.tools.CLIParams;
import com.raygroupintl.vista.tools.CLIParamsAdapter;

public class CLIRTParamsAdapter {
	public static RoutineToolParams toMRoutineToolParams(CLIParams params) {
		ParseTreeSupply pts = CLIParamsAdapter.getParseTreeSupply(params);
		return new RoutineToolParams(pts);
	}
		
	public static MRoutineToolInput toMRoutineInput(CLIParams params) {
		MRoutineToolInput input = new MRoutineToolInput();
		input.addRoutines(CLIParamsAdapter.getCLIRoutines(params));
		return input;
	}
}
