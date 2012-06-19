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

import java.util.List;
import java.util.Set;

import com.raygroupintl.m.parsetree.EntryId;
import com.raygroupintl.m.parsetree.FileWrapper;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.visitor.FanInRecorder;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.RepositoryVisitor;
import com.raygroupintl.vista.repository.VistaPackage;
import com.raygroupintl.vista.repository.VistaPackages;

public class FaninWriter extends RepositoryVisitor {
	private RepositoryInfo repositoryInfo;
	private FileWrapper fileWrapper;
	private FanInRecorder faninRecorder;
	
	public FaninWriter(RepositoryInfo repositoryInfo, FileWrapper fileWrapper) {
		this.repositoryInfo = repositoryInfo;
		this.fileWrapper = fileWrapper;
	}
		
	@Override
	protected void visitVistaPackage(VistaPackage routinePackage) {
		Filter<EntryId> filter = routinePackage.getPackageFanoutFilter();
		this.faninRecorder.setFilter(filter);
		super.visitVistaPackage(routinePackage);
	}

	public void visitRoutine(Routine routine) {
		routine.accept(this.faninRecorder);
	}
	
	protected void visitRoutinePackages(VistaPackages rps) {
		this.faninRecorder = new FanInRecorder();
		List<VistaPackage> packages = this.repositoryInfo.getAllPackages();
		for (VistaPackage p : packages) {
			p.accept(this);
		}
		Set<EntryId> fanins = this.faninRecorder.getFanIns();
		if (this.fileWrapper.start()) {
			for (EntryId f : fanins) {
				this.fileWrapper.write(f.toString());
				this.fileWrapper.writeEOL();
			}
			this.fileWrapper.stop();
		}
	}
}