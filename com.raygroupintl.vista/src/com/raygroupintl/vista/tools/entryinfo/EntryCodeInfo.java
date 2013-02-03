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

package com.raygroupintl.vista.tools.entryinfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.vista.tools.fnds.ToolResult;

public class EntryCodeInfo implements ToolResult {
	public EntryId entryId;
	public String[] formals;
	public Set<String> assumedLocals;
	public BasicCodeInfo otherCodeInfo;

	public EntryCodeInfo(EntryId entryId, String[] formals, Set<String> assumedLocals, BasicCodeInfo otherCodeInfo) {
		this.entryId = entryId;
		this.formals = formals;
		this.assumedLocals = assumedLocals;
		this.otherCodeInfo = otherCodeInfo;
	}
	
	@Override
	public void write(Terminal t, TerminalFormatter tf) {
		t.writeEOL(" " + this.entryId.toString2());		
		if ((this.formals == null) && (this.assumedLocals == null) && (this.otherCodeInfo == null)) {
			t.writeEOL("  ERROR: Invalid entry point");
			return;
		} else {
			t.writeFormatted("FORMAL", this.formals, tf);
			
			List<String> assumedLocalsSorted = new ArrayList<String>(this.assumedLocals);
			Collections.sort(assumedLocalsSorted);			
			t.writeFormatted("ASSUMED", assumedLocalsSorted, tf);
			
			t.writeFormatted("GLBS", this.otherCodeInfo.getGlobals(), tf);
			t.writeFormatted("READ" , this.otherCodeInfo.getReadCount(), tf);
			t.writeFormatted("WRITE", this.otherCodeInfo.getWriteCount(), tf);
			t.writeFormatted("EXEC", this.otherCodeInfo.getExecuteCount(), tf);
			t.writeFormatted("IND", this.otherCodeInfo.getIndirectionCount(), tf);
			t.writeFormatted("FMGLBS", this.otherCodeInfo.getFilemanGlobals(), tf);
			t.writeFormatted("FMCALLS", this.otherCodeInfo.getFilemanCalls(), tf);
			t.writeEOL();
		}
	}
}
