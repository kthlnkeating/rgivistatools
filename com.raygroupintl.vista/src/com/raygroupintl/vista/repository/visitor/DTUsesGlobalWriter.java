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

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.visitor.GlobalRecorder;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.struct.ExcludeValueFilter;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.RepositoryVisitor;
import com.raygroupintl.vista.repository.VistaPackages;
import com.raygroupintl.vista.repository.VistaPackage;

public class DTUsesGlobalWriter extends RepositoryVisitor {
	private RepositoryInfo repositoryInfo;
	private FileWrapper fileWrapper;
	private GlobalRecorder recorder = new GlobalRecorder();
	private String lastPackageName;
	
	public DTUsesGlobalWriter(RepositoryInfo repositoryInfo, FileWrapper fileWrapper) {
		this.repositoryInfo = repositoryInfo;
		this.fileWrapper = fileWrapper;
		ExcludeValueFilter<String> filter = new ExcludeValueFilter<String>();
		filter.add("TMP");
		filter.add("XTMP");
		filter.add("UTILITY");
		filter.add("%ZOSF");
		this.recorder.setFilter(filter);
	}
		
	@Override
	public void visitRoutine(Routine routine) {
		this.recorder.reset();
		routine.accept(recorder);
		ArrayList<String> result = new ArrayList<String>(recorder.getGlobals());
		Collections.sort(result);
		String routinePrefix = "Routine " + routine.getName() + ": ";
		for (String r : result) {
			String g = r;
			this.fileWrapper.write(routinePrefix);
			this.fileWrapper.write(r);
			this.fileWrapper.write(" ");
			String packageName = this.repositoryInfo.getPackageFromGlobal(g);
			if (packageName == null) {
				g = g.split("\\(")[0] + "(";
				packageName = this.repositoryInfo.getPackageFromGlobal(g);
			}
			if (packageName == null) {
				this.fileWrapper.write("(Dependency Unknown)");				
			} else if (! packageName.equalsIgnoreCase(this.lastPackageName)) {
				String fileName = this.repositoryInfo.getFileNameFromGlobal(g);
				this.fileWrapper.write("(Dependency to ");
				this.fileWrapper.write(packageName);
				this.fileWrapper.write(", ");
				if ((fileName != null) && (! fileName.isEmpty())) {
					this.fileWrapper.write(fileName);					
				} else {
					this.fileWrapper.write("Unknown");
				}
				this.fileWrapper.write(" File)");					
			}
			this.fileWrapper.writeEOL();
		}
	}
	
	@Override
	protected void visitVistaPackage(VistaPackage routinePackage) {
		this.fileWrapper.writeEOL("Directly Used Globals By " + routinePackage.getPackageName());
		this.fileWrapper.writeEOL();
		this.lastPackageName = routinePackage.getPackageName();
		super.visitVistaPackage(routinePackage);
		this.fileWrapper.writeEOL();
	}

	@Override
	protected void visitRoutinePackages(VistaPackages rps) {
		if (this.fileWrapper.start()) {
			super.visitRoutinePackages(rps);
			this.fileWrapper.stop();
		}
	}
}
