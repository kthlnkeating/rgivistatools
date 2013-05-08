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

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.tool.routine.EntryFanoutTool;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.vista.tools.CLIParams;
import com.raygroupintl.vista.tools.CLIParamsAdapter;

class CLIFanoutTool extends CLIRoutineTool {		
	public CLIFanoutTool(CLIParams params) {
		super(params);
	}
	
	@Override
	public void run() {
		List<Routine> routines = this.getRoutines();
		if (routines != null) {
			FileWrapper fr = CLIParamsAdapter.getOutputFile(this.params);
			if (fr != null) {
				EntryFanoutTool efa = new EntryFanoutTool();
				efa.addRoutines(routines);
				if (fr.start()) {
					TerminalFormatter tf = new TerminalFormatter();
					tf.setTab(12);
					efa.write(fr, tf);
					fr.stop();
				}
			}						
		}
	}
}
