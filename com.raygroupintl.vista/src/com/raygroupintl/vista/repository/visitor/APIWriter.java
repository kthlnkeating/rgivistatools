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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.Blocks;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.output.TerminalFormatter;

public class APIWriter {
	private FileWrapper fileWrapper;
	private Map<String, Blocks> routineBlocks;
	private TerminalFormatter tf = new TerminalFormatter();
	
	public APIWriter(FileWrapper fileWrapper, Map<String, Blocks> routineBlocks) {
		this.fileWrapper = fileWrapper;
		this.routineBlocks = routineBlocks;
	}

	private void write(EntryId entryId) {
		this.tf.setTab(12);
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
				this.fileWrapper.write(this.tf.startList("INPUT"));
				for (String input : inputs) {
					this.fileWrapper.write(this.tf.addToList(input));
				}
				this.fileWrapper.writeEOL();
			}
		}				
		this.fileWrapper.writeEOL();
		this.fileWrapper.writeEOL();		
	}
	
	public void auxWrite(String fanInFileName) {		
		try {
			int packageCount = 0;
			Path path = Paths.get(fanInFileName);
			Scanner scanner = new Scanner(path);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.endsWith(":")) {
					++packageCount;
					String name = line.substring(0, line.length()-1);
					this.fileWrapper.writeEOL();
					this.fileWrapper.writeEOL("--------------------------------------------------------------");
					this.fileWrapper.writeEOL();
					this.fileWrapper.writeEOL(String.valueOf(packageCount) + ". PACKAGE NAME: " + name);
					this.fileWrapper.writeEOL();
				} else {
					String[] pieces = line.split("\\^");
					if ((pieces != null) && (pieces.length > 0)) {
						String label = pieces[0];
						String routineName = pieces.length > 1 ? pieces[1] : null;
						EntryId entryId = new EntryId(routineName, label);
						this.write(entryId);
					}
				}
			}		
			if (packageCount > 0) {
				this.fileWrapper.writeEOL();
				this.fileWrapper.writeEOL("--------------------------------------------------------------");
			}
			scanner.close();
		} catch (IOException e) {
			this.fileWrapper.writeEOL("Unable to open file " + fanInFileName);
		}
	}
		
	public void write(String fanInFileName) {
		if (this.fileWrapper.start()) {
			this.auxWrite(fanInFileName);
			this.fileWrapper.stop();
		}
	}

	public void write(List<EntryId> entryIds) {
		if (this.fileWrapper.start()) {
			for (EntryId entryId : entryIds) {	
				this.write(entryId);
			}			
			this.fileWrapper.stop();
		}
	}
}
