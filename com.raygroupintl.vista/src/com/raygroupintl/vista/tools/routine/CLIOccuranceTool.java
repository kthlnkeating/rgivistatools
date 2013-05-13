//---------------------------------------------------------------------------
//Copyright 2012 Ray Group International
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//---------------------------------------------------------------------------

package com.raygroupintl.vista.tools.routine;

import java.util.List;

import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.ResultsByRoutine;
import com.raygroupintl.m.tool.routine.MRoutineToolInput;
import com.raygroupintl.m.tool.routine.occurance.Occurance;
import com.raygroupintl.m.tool.routine.occurance.OccuranceTool;
import com.raygroupintl.m.tool.routine.occurance.OccuranceToolParams;
import com.raygroupintl.m.tool.routine.occurance.OccuranceType;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.vista.tools.CLIParams;
import com.raygroupintl.vista.tools.CLIParamsAdapter;

public class CLIOccuranceTool extends CLIResultsByRoutineLabelTool<Occurance, List<Occurance>> {
	public CLIOccuranceTool(CLIParams params) {
		super(params);
	}
	
	private String getOccuranceTypeAsString(OccuranceType type) {
		switch (type) {
		case WRITE: return "Write";
		case READ: return "Read";
		case INDIRECTION: return "Indirection";
		case EXECUTE: return "Execute";
		case DOLLAR_TEXT: return "$TEXT";
		case NAKED_GLOBAL: return "Naked Global";
		default: return "";
		}
	}
	
	@Override
	protected void write(Terminal t, String indent,  Occurance result) {
		int lineIndex = result .getLineIndex();
		String location = "Line " + String.valueOf(lineIndex);
		String type = this.getOccuranceTypeAsString(result.getType());
		t.writeEOL(indent + location + " --> " + type);
	}

	@Override
	public void run() {
		Terminal t = CLIParamsAdapter.getTerminal(this.params);
		if (t != null) {
			ParseTreeSupply pts = CLIParamsAdapter.getParseTreeSupply(this.params);
			OccuranceToolParams p = new OccuranceToolParams(pts);	
			OccuranceTool tool = new OccuranceTool(p);
			MRoutineToolInput input = CLIRTParamsAdapter.toMRoutineInput(this.params);
			ResultsByRoutine<Occurance, List<Occurance>> result = tool.getResult(input);
			this.write(result, t);
		}
	}
}
