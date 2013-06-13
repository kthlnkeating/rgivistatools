//---------------------------------------------------------------------------
//Copyright 2013 PwC
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
import com.raygroupintl.output.FileTerminal;
import com.raygroupintl.vista.repository.visitor.EntryWriter;
import com.raygroupintl.vista.tools.CLIParams;
import com.raygroupintl.vista.tools.CLIParamsAdapter;

class CLIEntryListTool extends CLIRoutineTool {		
	public  CLIEntryListTool(CLIParams params) {
		super(params);
	}
	
	@Override
	public void run() {
		List<Routine> routines = this.getRoutines();
		if (routines != null) {
			FileTerminal fr = CLIParamsAdapter.getOutputFile(this.params);
			if (fr != null) {
				EntryWriter ew = new EntryWriter(fr);
				ew.writeForRoutines(routines);
			}						
		}
	}
}
