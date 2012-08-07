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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.visitor.FanoutRecorder;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.RepositoryVisitor;
import com.raygroupintl.vista.repository.VistaPackages;
import com.raygroupintl.vista.repository.VistaPackage;

public class FanoutWriter extends RepositoryVisitor {
	private FileWrapper fileWrapper;
	private RepositoryInfo repositoryInfo;
	private int packageCount;
	private Set<EntryId> packageFanouts;
	private FanoutRecorder recorder;
	private int numPackages;
	
	public FanoutWriter(FileWrapper fileWrapper, RepositoryInfo repositoryInfo) {
		this.fileWrapper = fileWrapper;
		this.repositoryInfo = repositoryInfo;
	}
		
	@Override
	protected void visitVistaPackage(VistaPackage routinePackage) {
		this.packageFanouts = new HashSet<EntryId>();
		this.recorder = new FanoutRecorder(routinePackage.getPackageFanoutFilter());
		
		super.visitVistaPackage(routinePackage);
		if (this.packageFanouts.size() > 0) {
			++this.packageCount;
			if (this.numPackages > 1) {
				this.fileWrapper.writeEOL("--------------------------------------------------------------");
				this.fileWrapper.writeEOL();
				this.fileWrapper.writeEOL(String.valueOf(this.packageCount) + ". PACKAGE NAME: " + routinePackage.getPackageName());
				this.fileWrapper.writeEOL();
			} else {
				this.fileWrapper.writeEOL("PACKAGE NAME: " + routinePackage.getPackageName());
				this.fileWrapper.writeEOL();				
			}
			
			List<EntryId> result = new ArrayList<EntryId>(this.packageFanouts);
			Collections.sort(result);
			Map<String, List<EntryId>> resultByPackage = new HashMap<String, List<EntryId>>();
			for (EntryId eid : result) {
				String routineName = eid.getRoutineName();
				VistaPackage pkg = this.repositoryInfo.getPackageFromRoutineName(routineName);
				String prefix = pkg.getPrimaryPrefix();
				List<EntryId> pkgEntryIds = resultByPackage.get(prefix);
				if (pkgEntryIds == null) {
					pkgEntryIds = new ArrayList<EntryId>();
					resultByPackage.put(prefix, pkgEntryIds);
				}
				pkgEntryIds.add(eid);
			}
			
			List<String> keys = new ArrayList<String>(resultByPackage.keySet());
			Collections.sort(keys);
			for (String key : keys) {
				List<EntryId> pkgEntryIds = resultByPackage.get(key);
				for (EntryId eid : pkgEntryIds) {
					String info = " " + eid.toString();
					String routineName = eid.getRoutineName();
					VistaPackage pkg = this.repositoryInfo.getPackageFromRoutineName(routineName);
					info += " (" + pkg.getPackageName() + ")";
					this.fileWrapper.write(info);
					this.fileWrapper.writeEOL();							
				}
			}
			if (this.numPackages > 1) {		
				this.fileWrapper.writeEOL();
				this.fileWrapper.writeEOL("--------------------------------------------------------------");
				this.fileWrapper.writeEOL();
			}
		}
	}

	public void visitRoutine(Routine routine) {
		routine.accept(this.recorder);
		Map<LineLocation, List<EntryId>> fanouts = this.recorder.getRoutineFanouts();
		if (fanouts != null) {
			for (List<EntryId> fs : fanouts.values()) {
				for (EntryId f : fs) {			
					this.packageFanouts.add(f);
				}
			}
		}
	}
	
	protected void visitRoutinePackages(VistaPackages rps) {
		this.numPackages = rps.getPackagesSize();
		if (this.fileWrapper.start()) {
			rps.acceptSubNodes(this);
			this.fileWrapper.stop();
		}
	}
}
