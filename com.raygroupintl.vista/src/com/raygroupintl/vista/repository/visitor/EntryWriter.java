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

package com.raygroupintl.vista.repository.visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.visitor.EntryRecorder;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.vista.repository.RepositoryVisitor;
import com.raygroupintl.vista.repository.VistaPackage;
import com.raygroupintl.vista.repository.VistaPackages;

public class EntryWriter extends RepositoryVisitor {
	private FileWrapper fileWrapper;
	private EntryRecorder entryRecorder;
	private List<String> nameRegexs;
	
	public EntryWriter(FileWrapper fileWrapper) {
		this.fileWrapper = fileWrapper;
	}
	
	public void addRoutineNameFilter(String regex) {
		if (this.nameRegexs == null) {
			this.nameRegexs = new ArrayList<String>();
		}
		this.nameRegexs.add(regex);
	}
	
	
	private boolean matches(String name) {
		for (String nameRegex : this.nameRegexs) {
			if (name.matches(nameRegex)) return true;
		}
		return false;
	}
	
	@Override
	protected void visitRoutine(Routine routine) {
		if ((this.nameRegexs == null) || (this.matches(routine.getName()))) {
			this.entryRecorder.visitRoutine(routine);
		}
	}
	
	@Override
	protected void visitVistaPackage(VistaPackage routinePackage) {
		routinePackage.acceptSubNodes(this);
	}

	private void writeEntries() {
		List<EntryId> entries = new ArrayList<EntryId>(this.entryRecorder.getEntries());
		Collections.sort(entries);
		for (EntryId entry : entries) {
			String entryString = entry.toString();
			this.fileWrapper.writeEOL(entryString);
		}		
	}
	
	@Override
	protected void visitRoutinePackages(VistaPackages rps) {
		if (this.fileWrapper.start()) {
			this.entryRecorder = new EntryRecorder();
			super.visitRoutinePackages(rps);
			this.writeEntries();
			this.fileWrapper.stop();
		}
	}
	
	public void writeForRoutines(List<Routine> routines) {
		if (this.fileWrapper.start()) {
			this.entryRecorder = new EntryRecorder();
			for (Routine r : routines) {
				this.visitRoutine(r);
			}
			this.writeEntries();
			this.fileWrapper.stop();
		}		
	}
}