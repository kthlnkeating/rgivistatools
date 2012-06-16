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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.Fanout;
import com.raygroupintl.m.parsetree.FileWrapper;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.RoutinePackage;
import com.raygroupintl.m.parsetree.RoutinePackages;
import com.raygroupintl.m.struct.LineLocation;

public class FanoutWriter extends FanoutRecorder {
	private FileWrapper fileWrapper;
	private int packageCount;
	private Set<Fanout> packageExisting;
	
	public FanoutWriter(FileWrapper fileWrapper) {
		this.fileWrapper = fileWrapper;
	}
		
	protected void visitRoutinePackage(RoutinePackage routinePackage) {
		++this.packageCount;
		this.packageExisting = new HashSet<Fanout>();
		
		this.fileWrapper.write("--------------------------------------------------------------");
		this.fileWrapper.writeEOL();
		this.fileWrapper.writeEOL();
		this.fileWrapper.write(String.valueOf(this.packageCount) + ". " + routinePackage.getPackageName());
		this.fileWrapper.writeEOL();

		super.visitRoutinePackage(routinePackage);
		
		this.fileWrapper.write("--------------------------------------------------------------");
		this.fileWrapper.writeEOL();
		this.fileWrapper.writeEOL();
	}

	public void visitRoutine(Routine routine) {
		super.visitRoutine(routine);
		Map<LineLocation, List<Fanout>> fanouts = this.getRoutineFanouts();
		if (fanouts != null) {
			Set<Fanout> result = new HashSet<Fanout>();
			for (List<Fanout> fs : fanouts.values()) {
				for (Fanout f : fs) {			
					if (! this.packageExisting.contains(f)) {
						result.add(f);
						this.packageExisting.add(f);
					}
				}
			}
			for (Fanout fout : result) {
				String info = fout.toString();
				info += " (" + routine.getKey() + ")";
				this.fileWrapper.write(info);
				this.fileWrapper.writeEOL();
			}
		}
	}
	
	protected void visitRoutinePackages(RoutinePackages rps) {
		if (this.fileWrapper.start()) {
			rps.acceptSubNodes(this);
			this.fileWrapper.stop();
		}
	}
}
