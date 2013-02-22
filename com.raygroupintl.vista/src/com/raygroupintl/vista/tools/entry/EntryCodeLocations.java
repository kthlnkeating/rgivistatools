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

import java.io.Serializable;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.vista.tools.fnds.ToolResult;

public class EntryCodeLocations implements ToolResult, Serializable {
	private static final long serialVersionUID = 1L;

	private EntryId entryUnderTest;
	private CodeLocations codeLocations;

	public EntryCodeLocations(EntryId entryUnderTest) {
		this(entryUnderTest, new CodeLocations());
	}
	
	public EntryCodeLocations(EntryId entryUnderTest, CodeLocations codeLocations) {
		this.entryUnderTest = entryUnderTest;
		this.codeLocations = codeLocations;
	}
	
	public EntryId getEntry() {
		return this.entryUnderTest;
	}
	
	public CodeLocations getCodeLocations() {
		return this.codeLocations;
	}
	
	@Override
	public void write(Terminal t, TerminalFormatter tf) {
		t.writeEOL(" " + this.entryUnderTest.toString2());
		this.codeLocations.write(t, tf);
	}	
}
