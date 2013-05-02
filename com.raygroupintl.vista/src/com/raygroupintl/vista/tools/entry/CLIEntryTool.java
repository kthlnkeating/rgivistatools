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

package com.raygroupintl.vista.tools.entry;

import java.util.List;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.tool.MToolError;
import com.raygroupintl.m.tool.entry.MEntryToolIndividualResult;
import com.raygroupintl.m.tool.entry.MEntryToolInput;
import com.raygroupintl.m.tool.entry.MEntryToolResult;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.vista.tools.CLIParams;
import com.raygroupintl.vista.tools.CLIParamsAdapter;
import com.raygroupintl.vista.tools.Tool;

public abstract class CLIEntryTool<U extends MEntryToolIndividualResult> extends Tool {		
	protected CLIParams params;
	
	public CLIEntryTool(CLIParams params) {
		super(params);
		this.params = params;
	}
	
	protected abstract MEntryToolResult<U> getResult(MEntryToolInput input);
	
	protected void write(EntryId entryId, U result, Terminal t, TerminalFormatter tf, boolean skipEmpty) {			
		if (result.isValid()) {
			if ((! skipEmpty) || (! result.isEmpty())) {
				t.writeEOL(" " + entryId.toString2());		
				this.write(result, t, tf);
			}
		} else {
			t.writeEOL(" " + entryId.toString2());		
			t.writeEOL("  ERROR: Invalid entry point");
			return;
		}
		
	}
			
	protected abstract void write(U result, Terminal t, TerminalFormatter tf);

	private boolean skipEmpty() {
		List<String> outputFlags = this.params.outputFlags;
		for (String outputFlag : outputFlags) {
			if (outputFlag.equals("ignorenodata")) {
				return true;
			}
		}
		return false;			
	}
	
	@Override
	public void run() {
		Terminal t = CLIParamsAdapter.getTerminal(this.params);
		if (t != null) {
			MEntryToolInput input = CLIETParamsAdapter.getMEntryToolInput(this.params);
			if (input != null) {
				MEntryToolResult<U> result = this.getResult(input);		
				if (t.start()) {
					TerminalFormatter tf = new TerminalFormatter();
					tf.setTab(12);
					List<EntryId> entries = result.getEntries();
					List<U> resultList = result.getResults();
					int n = entries.size();
					for (int i=0; i<n; ++i) {
						U u = resultList.get(i);
						if (u != null) {
							this.write(entries.get(i), resultList.get(i), t, tf, this.skipEmpty());
						} else {
							t.writeEOL(" " + entries.get(i).toString2());
							MToolError error = result.getError(i);
							t.writeEOL("  ERROR: " + error.getMessage());
						}
					}
					t.stop();
				}
			}
		}
	}
}

