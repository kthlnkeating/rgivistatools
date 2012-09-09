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

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.visitor.CacheOccuranceRecorder;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.vista.repository.RepositoryVisitor;
import com.raygroupintl.vista.repository.VistaPackages;

public class CacheOccuranceWriter extends RepositoryVisitor {
	private FileWrapper fileWrapper;
	
	public CacheOccuranceWriter(FileWrapper fileWrapper) {
		this.fileWrapper = fileWrapper;
	}
		
	@Override
	protected void visitRoutine(Routine routine) {
		CacheOccuranceRecorder cor = new CacheOccuranceRecorder();
		cor.visitRoutine(routine);
		if (cor.getNumOccurance() > 0) {
			this.fileWrapper.writeEOL(routine.getName());
		}
	}
	
	@Override
	protected void visitRoutinePackages(VistaPackages rps) {
		if (this.fileWrapper.start()) {
			super.visitRoutinePackages(rps);
			this.fileWrapper.stop();
		}
	}
}