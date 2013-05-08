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

import java.util.Set;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.tool.CommonToolParams;
import com.raygroupintl.m.tool.entry.MEntryToolInput;
import com.raygroupintl.m.tool.entry.MEntryToolResult;
import com.raygroupintl.m.tool.entry.fanin.EntryFanins;
import com.raygroupintl.m.tool.entry.fanin.FaninTool;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.vista.tools.CLIParams;

public class CLIFaninTool extends CLIEntryTool<EntryFanins> {		
	public CLIFaninTool(CLIParams params) {
		super(params);
	}
	
	@Override
	public MEntryToolResult<EntryFanins> getResult(MEntryToolInput input) {
		MEntryToolResult<EntryFanins> resultList = new MEntryToolResult<EntryFanins>();
		for (EntryId entryId : input.getEntryIds()) {
			CommonToolParams tp = CLIETParamsAdapter.toCommonToolParams(this.params);
			FaninTool efit = new FaninTool(entryId, tp, true);
			EntryFanins result = efit.getResult();
			resultList.add(entryId, result);
		}
		return resultList;
	}
	
	@Override
	protected void write(EntryFanins result, Terminal t, TerminalFormatter tf) {
		Set<EntryId> starts = result.getFaninEntries();
		for (EntryId start : starts) {
			Set<EntryId> nextUps = result.getFaninNextEntries(start);
			for (EntryId nextUp : nextUps) {
				t.write("   " + start.toString2() + " thru ");
				t.writeEOL(nextUp.toString2());
			}
		}	
	}
}
