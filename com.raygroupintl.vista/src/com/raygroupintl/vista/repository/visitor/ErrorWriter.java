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

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.visitor.ErrorRecorder;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.m.struct.MError;
import com.raygroupintl.m.struct.ObjectInRoutine;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.vista.repository.RepositoryVisitor;
import com.raygroupintl.vista.repository.VistaPackages;
import com.raygroupintl.vista.repository.VistaPackage;
import com.raygroupintl.vista.tools.ErrorExemptions;

public class ErrorWriter extends RepositoryVisitor {
	private FileWrapper fileWrapper;
	private int errorCount;
	private int packageCount;
	private ErrorRecorder errorRecorder;
	
	public ErrorWriter(ErrorExemptions exemptions, FileWrapper fileWrapper) {
		this.errorRecorder = new ErrorRecorder(exemptions);
		this.fileWrapper = fileWrapper;
	}
	
	public ErrorWriter(FileWrapper fileWrapper) {
		this.errorRecorder = new ErrorRecorder();
		this.fileWrapper = fileWrapper;
	}

	@Override
	protected void visitVistaPackage(VistaPackage routinePackage) {
		++this.packageCount;
		
		this.fileWrapper.write("--------------------------------------------------------------");
		this.fileWrapper.writeEOL();
		this.fileWrapper.writeEOL();
		this.fileWrapper.write(String.valueOf(this.packageCount) + ". " + routinePackage.getPackageName());
		this.fileWrapper.writeEOL();

		super.visitVistaPackage(routinePackage);
		
		this.fileWrapper.write("--------------------------------------------------------------");
		this.fileWrapper.writeEOL();
		this.fileWrapper.writeEOL();
	}

	@Override
	protected void visitRoutine(Routine routine) {
		routine.accept(this.errorRecorder);
		List<ObjectInRoutine<MError>> errors = this.errorRecorder.getLastErrors();
		if (errors.size() > 0) {
			this.fileWrapper.writeEOL();
			this.fileWrapper.writeEOL();
			this.fileWrapper.write(routine.getName());
			this.fileWrapper.writeEOL();

			LineLocation lastLocation = new LineLocation("", 0);
			for (ObjectInRoutine<MError> error: errors) {
				lastLocation = error.getLocation();
				if (lastLocation == null) {
					this.fileWrapper.write("  Structural error");							
					this.fileWrapper.writeEOL();
				} else {
					String offset = (lastLocation.getOffset() == 0 ? "" : '+' + String.valueOf(lastLocation.getOffset()));
					this.fileWrapper.write("  " + lastLocation.getTag() + offset);
					this.fileWrapper.writeEOL();
				} 
				this.fileWrapper.write("    " + error.getObject().getText());
				this.fileWrapper.writeEOL();
			}
			this.errorCount += errors.size();
		}		
	}
	
	protected void visitRoutinePackages(VistaPackages rps) {
		if (this.fileWrapper.start()) {
			rps.acceptSubNodes(this);
			this.fileWrapper.writeEOL();
			this.fileWrapper.writeEOL();
			this.fileWrapper.write("Number Errors: " + String.valueOf(this.errorCount));			
			this.fileWrapper.writeEOL();
			this.fileWrapper.stop();
		}
	}
}
