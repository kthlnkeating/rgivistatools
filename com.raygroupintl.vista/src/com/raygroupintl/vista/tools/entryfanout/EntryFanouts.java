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

package com.raygroupintl.vista.tools.entryfanout;

import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.vista.tools.fnds.ToolResult;

public class EntryFanouts implements ToolResult {
	private EntryId entryUnderTest;
	private SortedSet<EntryId> fanoutEntries;

	public EntryFanouts(EntryId entryUnderTest) {
		this.entryUnderTest = entryUnderTest;
	}
	
	public void add(EntryId fanout) {
		if (this.fanoutEntries == null) {
			this.fanoutEntries = new TreeSet<EntryId>();
		} 
		this.fanoutEntries.add(fanout);
	}
	
	public EntryId getEntry() {
		return this.entryUnderTest;
	}
	
	public Set<EntryId> getFanouts() {
		if (this.fanoutEntries == null) {
			return Collections.emptySet();
		} else {
			return Collections.unmodifiableSortedSet(this.fanoutEntries);
		}
	}
	
	@Override
	public void write(Terminal t, TerminalFormatter tf) {
		t.writeEOL(" " + this.entryUnderTest.toString2());	
		if (fanoutEntries == null) {
			t.writeEOL("  --");				
		} else {
			for (EntryId f : this.fanoutEntries) {
				t.writeEOL("  " + f.toString2());
			}
		}
	}	
}
