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

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.m.parsetree.FileWrapper;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.RoutineFactory;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.m.struct.MError;
import com.raygroupintl.m.struct.ObjectInRoutine;
import com.raygroupintl.vista.repository.FileSupply;
import com.raygroupintl.vista.tools.ErrorExemptions;

public class ErrorWriter extends ErrorRecorder {
	private final static Logger LOGGER = Logger.getLogger(ErrorWriter.class.getName());
	
	private FileWrapper fileWrapper;
	private int errorCount;
	
	public ErrorWriter(ErrorExemptions exemptions, FileWrapper fileWrapper) {
		super(exemptions);
		this.fileWrapper = fileWrapper;
	}
	
	public ErrorWriter(FileWrapper fileWrapper) {
		this.fileWrapper = fileWrapper;
	}

	@Override
	protected void visitRoutine(Routine routine) {
		super.visitRoutine(routine);
		List<ObjectInRoutine<MError>> errors = this.getLastErrors();
		if (errors.size() > 0) {
			this.fileWrapper.writeEOL();
			this.fileWrapper.writeEOL();
			this.fileWrapper.write(routine.getKey());
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
	
	public void write(RoutineFactory rf) {
		try {
			this.fileWrapper.start();
			this.fileWrapper.writeEOL();
			this.errorCount = 0;
			List<Path> paths = FileSupply.getAllMFiles();
			for (Path path : paths) {
				Routine routine = (Routine) rf.getNode(path);
				routine.accept(this);
			}
			this.fileWrapper.writeEOL();
			this.fileWrapper.writeEOL();
			this.fileWrapper.write("Number Errors: " + String.valueOf(this.errorCount));			
			this.fileWrapper.writeEOL();
			this.fileWrapper.stop();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Unable to open file.");			
		}
	}
	
}
