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
import com.raygroupintl.m.parsetree.visitor.FilemanCallRecorder;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.RepositoryVisitor;
import com.raygroupintl.vista.repository.VistaPackages;
import com.raygroupintl.vista.repository.VistaPackage;

public class DTFilemanCallWriter extends RepositoryVisitor {
	private RepositoryInfo repositoryInfo;
	private FileWrapper fileWrapper;
	private int pkgCount;
	private String lastPackageName;
	private boolean packageFirst;
	
	public DTFilemanCallWriter(RepositoryInfo repositoryInfo, FileWrapper fileWrapper) {
		this.repositoryInfo = repositoryInfo;
		this.fileWrapper = fileWrapper;
	}
		
	private void writeData(TerminalFormatter tf, List<String> dataList, String title) {
		this.fileWrapper.write(tf.startList(title));
		if (dataList.size() > 0) {
			for (String data : dataList) {
				this.fileWrapper.write(tf.addToList(data));
			}
		} else {
			this.fileWrapper.write("--");
		}
		this.fileWrapper.writeEOL();		
	}
	
	@Override
	public void visitRoutine(Routine routine) {
		FilemanCallRecorder recorder = new FilemanCallRecorder(this.repositoryInfo);
		routine.accept(recorder);
		List<String> filemanGlobals = recorder.getFilemanGlobals();
		List<String> filemanCalls = recorder.getFilemanCalls();
		if ((filemanCalls.size() > 0) || (filemanGlobals.size() > 0)) {	
			if (this.packageFirst) {
				this.packageFirst = false;
				++this.pkgCount;
				this.fileWrapper.writeEOL("--------------------------------------------------------------");
				this.fileWrapper.writeEOL();
				this.fileWrapper.write(String.valueOf(this.pkgCount));
				this.fileWrapper.write(". PACKAGE NAME: ");
				this.fileWrapper.writeEOL(this.lastPackageName);
				this.fileWrapper.writeEOL();
			}			
			this.fileWrapper.writeEOL(" " + routine.getName());
			TerminalFormatter tf = new TerminalFormatter();
			tf.setTab(17);
			this.writeData(tf, filemanGlobals, "Globals");
			this.writeData(tf, filemanCalls, "FileMan calls");
			this.fileWrapper.writeEOL();
		}
	}
	
	@Override
	protected void visitVistaPackage(VistaPackage routinePackage) {
		String prefix = routinePackage.getDefaultPrefix();
		if ((! prefix.equals("DI")) && (! prefix.equals("XU")) && (! prefix.equals("UNCATEGORIZED"))) {		
			this.lastPackageName = routinePackage.getPackageName();
			this.packageFirst = true;
			super.visitVistaPackage(routinePackage);
			if (! this.packageFirst) {
				this.fileWrapper.writeEOL("--------------------------------------------------------------");
				this.fileWrapper.writeEOL();				
			}
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
