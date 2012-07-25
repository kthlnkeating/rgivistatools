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

package com.raygroupintl.m.parsetree.visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.raygroupintl.m.parsetree.DoBlock;
import com.raygroupintl.m.parsetree.Entry;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.Visitor;
import com.raygroupintl.m.parsetree.data.EntryId;

public class EntryRecorder extends Visitor {
	private String routineName;
	private List<EntryId> entries = new ArrayList<EntryId>();
	private int inDoBlock;
	
	protected void visitEntry(Entry entry) {
		if (this.inDoBlock == 0) {
			String tag = entry.getName();
			EntryId entryId = new EntryId(this.routineName, tag);
			this.entries.add(entryId);
		}
		super.visitEntry(entry);		
	}

	@Override
	protected void visitDoBlock(DoBlock doBlock) {
		++this.inDoBlock;
		super.visitDoBlock(doBlock);
		--this.inDoBlock;
	}

	@Override
	public void visitRoutine(Routine routine) {
		this.routineName = routine.getName();
		super.visitRoutine(routine);
	}
	
	public void reset() {
		this.entries = new ArrayList<EntryId>();
		this.routineName = null;
	}
	
	public List<EntryId> getEntries() {
		return Collections.unmodifiableList(this.entries);
	}
}
