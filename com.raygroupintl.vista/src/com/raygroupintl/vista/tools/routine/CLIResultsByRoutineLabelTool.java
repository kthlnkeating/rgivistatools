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
import java.util.Set;

import com.raygroupintl.m.tool.ResultsByLabel;
import com.raygroupintl.m.tool.ResultsByRoutine;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.vista.tools.CLIParams;
import com.raygroupintl.vista.tools.CLIParamsAdapter;
import com.raygroupintl.vista.tools.OutputFlags;

abstract class CLIResultsByRoutineLabelTool<T, U extends Collection<T>> extends CLIRoutineTool {
	private final String INDENT = "  ";
	private boolean skipEmpty;
	
	public CLIResultsByRoutineLabelTool(CLIParams params) {
		super(params);
		OutputFlags ofs = CLIParamsAdapter.toOutputFlags(params);
		this.skipEmpty = ofs.getSkipEmpty(false);
	}
	
	protected abstract void write(Terminal t, String indent, T result) throws IOException;

	private void innerWrite(ResultsByRoutine<T, U> results, Terminal t) throws IOException {
		Set<String> rns = results.getRoutineNames();
		for (String rn : rns) {
			ResultsByLabel<T, U> rsbl = results.getResults(rn);
			Set<String> labels = rsbl.getLabels();
			for (String label : labels) {
				U rs = rsbl.getResults(label);
				if ((rs == null) || (rs.size() == 0)) {
					if (! this.skipEmpty) {
						t.writeEOL(INDENT + label + "^" + rn);	
						t.writeEOL(INDENT + INDENT + "--");
					}
				} else for (T r : rs) {
					t.writeEOL(INDENT + label + "^" + rn);	
					this.write(t, INDENT + INDENT, r);
				}
			}
		
		}
	}
	
	protected void write(ResultsByRoutine<T, U> results, Terminal t) throws IOException {
		t.getTerminalFormatter().setTab(12);
		this.innerWrite(results, t);
		t.stop();
	}
}
