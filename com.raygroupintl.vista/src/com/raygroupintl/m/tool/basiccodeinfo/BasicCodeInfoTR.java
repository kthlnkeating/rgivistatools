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

package com.raygroupintl.m.tool.basiccodeinfo;

import com.raygroupintl.m.tool.MEntryToolResult;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;

public class BasicCodeInfoTR implements MEntryToolResult {
	public String[] formals;	
	private BasicCodeInfo info;

	public BasicCodeInfoTR(String[] formals, BasicCodeInfo info) {
		this.formals = formals;
		this.info = info;
	}
	
	public String[] getFormals() {
		return this.formals;
	}
	
	public BasicCodeInfo getData() {
		return this.info;
	}
	
	@Override
	public boolean isValid() {
		return this.info != null;
	}
	
	@Override
	public boolean isEmpty() {
		return this.info == null;
	}
	
	public void writeInfo(Terminal t, TerminalFormatter tf) {
		t.writeFormatted("GLBS", this.info.getGlobals(), tf);
		t.writeFormatted("READ" , this.info.getReadCount(), tf);
		t.writeFormatted("WRITE", this.info.getWriteCount(), tf);
		t.writeFormatted("EXEC", this.info.getExecuteCount(), tf);
		t.writeFormatted("IND", this.info.getIndirectionCount(), tf);
		t.writeFormatted("FMGLBS", this.info.getFilemanGlobals(), tf);
		t.writeFormatted("FMCALLS", this.info.getFilemanCalls(), tf);		
	}
}
