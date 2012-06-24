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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.Blocks;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.vista.repository.RepositoryInfo;

public class APIWriter {
	//private RepositoryInfo repositoryInfo;
	private FileWrapper fileWrapper;
	private Map<String, Blocks> routineBlocks;
	
	public APIWriter(RepositoryInfo repositoryInfo, FileWrapper fileWrapper, Map<String, Blocks> routineBlocks) {
		//this.repositoryInfo = repositoryInfo;
		this.fileWrapper = fileWrapper;
		this.routineBlocks = routineBlocks;
	}
		
	public void write(List<EntryId> entryIds) {
		if (this.fileWrapper.start()) {
			TerminalFormatter f = new TerminalFormatter();
			f.setTab(12);
			for (EntryId entryId : entryIds) {				
				String routineName = entryId.getRoutineName();
				this.fileWrapper.write("Routine: " + routineName);
				this.fileWrapper.writeEOL();
				Blocks rbs = this.routineBlocks.get(routineName);
				if (rbs == null) {
					this.fileWrapper.write("  " + "ERROR: No routine API information is found for " + routineName);
					this.fileWrapper.writeEOL();
				} else {
					String label = entryId.getLabelOrDefault();
					Block lb = rbs.get(label);
					if (lb == null) {
						this.fileWrapper.write("  " + "ERROR: No entry API information is found for " + entryId.toString());
						this.fileWrapper.writeEOL();						
					} else {
						Set<EntryId> entryIfTrack = new HashSet<EntryId>();
						Set<String> inputs = lb.getUseds(this.routineBlocks, entryIfTrack);						
						this.fileWrapper.write(f.startList("INPUT"));
						for (String input : inputs) {
							this.fileWrapper.write(f.addToList(input));
						}
						this.fileWrapper.writeEOL();
					}
				}				
				this.fileWrapper.writeEOL();
				this.fileWrapper.writeEOL();
			}			
			this.fileWrapper.stop();
		}
	}
}
