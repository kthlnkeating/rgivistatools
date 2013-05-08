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

import com.raygroupintl.m.tool.CommonToolParams;
import com.raygroupintl.m.tool.entry.MEntryToolInput;
import com.raygroupintl.m.tool.entry.MEntryToolResult;
import com.raygroupintl.m.tool.entry.quittype.CallType;
import com.raygroupintl.m.tool.entry.quittype.CallTypeState;
import com.raygroupintl.m.tool.entry.quittype.QuitType;
import com.raygroupintl.m.tool.entry.quittype.QuitTypeState;
import com.raygroupintl.m.tool.entry.quittype.QuitTypeTool;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.vista.tools.CLIParams;

class CLIQuitTypeTool extends CLIEntryTool<QuitType> {	
	public CLIQuitTypeTool(CLIParams params) {
		super(params);		
	}
	
	@Override
	protected boolean isEmpty(QuitType quitType) {
		return ! quitType.hasConflict();
	}
			
	@Override
	protected MEntryToolResult<QuitType> getResult(MEntryToolInput input) {
		CommonToolParams params = CLIETParamsAdapter.toCommonToolParams(this.params);
		QuitTypeTool a = new QuitTypeTool(params);
		return a.getResult(input);			
	}
	
	@Override
	protected void write(QuitType result, Terminal t, TerminalFormatter tf) {
		boolean skipEmpty = this.skipEmpty();		
		QuitTypeState qts = result.getQuitTypeState();
		switch (qts) {
			case NO_QUITS:
				if (! skipEmpty) {
					t.writeFormatted("QUIT", "No quits.", tf);
				}
			break;
			case QUITS_WITH_VALUE:
				if (! skipEmpty) {
					String fl = result.getFirstQuitLocation().toString(); 
					t.writeFormatted("QUIT", "With value (ex: " + fl + ")", tf);
				}
			break;
			case QUITS_WITHOUT_VALUE:
				if (! skipEmpty) {
					String fl = result.getFirstQuitLocation().toString(); 
					t.writeFormatted("QUIT", "Without value (ex: " + fl + ")", tf);
				}
			break;
			case CONFLICTING_QUITS:
			{
				String fl = result.getFirstQuitLocation().toString(); 
				String cl = result.getConflictingLocation().toString();
				t.writeFormatted("QUIT", "Conflicted (ex: " + fl + " vs " + cl + ")", tf);
			}
			break;
		}
		for (CallType ct : result.getFanoutCalls().values()) {
			CallTypeState state = ct.getState();
			switch (state) {
			case DO_CONFLICTING:
			{
				String fl = ct.getLocation().toString(); 
				t.writeFormatted("CALL", "Invalid DO at " + fl, tf);				
			}				
			break;
			case EXTRINSIC_CONFLICTING:
			{
				String fl = ct.getLocation().toString(); 
				t.writeFormatted("CALL", "Invalid extrinsic at " + fl, tf);				
			}				
			break;
			case FANOUT_CONFLICTING:
			{
				String fl = ct.getLocation().toString(); 
				t.writeFormatted("CALL", "Fanout has invalid quit type at " + fl, tf);				
			}				
			break;			
			case DO_UNVERIFIED:
			case DO_VERIFIED:
				if (! skipEmpty) {
					String fl = ct.getLocation().toString(); 
					t.writeFormatted("CALL", "DO at " + fl, tf);									
				}
			break;
			case EXTRINSIC_UNVERIFIED:
			case EXTRINSIC_VERIFIED:
				if (! skipEmpty) {
					String fl = ct.getLocation().toString(); 
					t.writeFormatted("CALL", "Extrinsic at " + fl, tf);									
				}
			break;
			default:
				break;
			}			
		}
	}
}
