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

import java.util.Collection;
import java.util.Set;

import com.raygroupintl.m.tool.ResultsByLabel;
import com.raygroupintl.m.tool.ResultsByRoutine;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.vista.tools.CLIParams;

abstract class CLIResultsByRoutineLabelTool<T, U extends Collection<T>> extends CLIRoutineTool {
	private final String INDENT = "  ";
	
	public CLIResultsByRoutineLabelTool(CLIParams params) {
		super(params);
	}
	
	protected abstract String toString(T result);

	protected void write(ResultsByRoutine<T, U> results, Terminal t, TerminalFormatter tf) {
		Set<String> rns = results.getRoutineNames();
		for (String rn : rns) {
			ResultsByLabel<T, U> rsbl = results.getResults(rn);
			Set<String> labels = rsbl.getLabels();
			for (String label : labels) {
				t.writeEOL(INDENT + label + "^" + rn);	
				U rs = rsbl.getResults(label);
				if ((rs == null) || (rs.size() == 0)) {
					t.writeEOL(INDENT + INDENT + "--");				
				} else for (T r : rs) {
					t.writeEOL(INDENT + INDENT + this.toString(r));
				}
			}
		
		}
	}
	
	protected void write(ResultsByRoutine<T, U> results, Terminal t) {
		if (t.start()) {
			TerminalFormatter tf = new TerminalFormatter();
			tf.setTab(12);
			this.write(results, t, tf);
			t.stop();
		}								
	}
}
