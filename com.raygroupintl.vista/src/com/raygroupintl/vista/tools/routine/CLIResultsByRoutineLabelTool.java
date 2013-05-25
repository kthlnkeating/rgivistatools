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

import java.io.IOException;
import java.util.Collection;

import com.raygroupintl.m.tool.OutputFlags;
import com.raygroupintl.m.tool.ResultsByRoutine;
import com.raygroupintl.m.tool.ToolResultPiece;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.vista.tools.CLIParams;
import com.raygroupintl.vista.tools.CLIParamsAdapter;

abstract class CLIResultsByRoutineLabelTool<T extends ToolResultPiece, U extends Collection<T>> extends CLIRoutineTool {
	public OutputFlags ofs;
	
	public CLIResultsByRoutineLabelTool(CLIParams params) {
		super(params);
		this.ofs = CLIParamsAdapter.toOutputFlags(params);
	}
	
	protected void write(ResultsByRoutine<T, U> results, Terminal t) throws IOException {
		t.getTerminalFormatter().setTitleWidth(12);
		results.write(t, this.ofs);
		t.stop();
	}
}
