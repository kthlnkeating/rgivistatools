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

package com.raygroupintl.vista.tools;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.filter.ExcludeAllFanoutFilter;
import com.raygroupintl.m.parsetree.filter.ExcludeFilemanCallFanoutFilter;
import com.raygroupintl.m.parsetree.filter.ExcludeNonPkgCallFanoutFilter;
import com.raygroupintl.m.parsetree.filter.ExcludeNonRtnFanoutFilter;
import com.raygroupintl.m.parsetree.filter.PercentRoutineFanoutFilter;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.VistaPackage;
import com.raygroupintl.vista.repository.VistaPackages;
import com.raygroupintl.vista.tools.fnds.ToolResult;

public abstract class Tool {
	protected CLIParams params;
	protected Writer toolOutput; //can be set programatically (ie: eclipse)
	
	protected Tool(CLIParams params) {
		this.params = params;
	}
		
	public abstract void run();

	protected RepositoryInfo getRepositoryInfo() {
		MRARoutineFactory rf = MRARoutineFactory.getInstance(MVersion.CACHE);
		if (rf != null) {
			RepositoryInfo ri = RepositoryInfo.getInstance(rf);
			if (ri != null) {
				ri.addMDirectories(params.additionalMDirectories);
				ri.addMFiles(params.additionalMFiles);
				if ((params.ownershipFilePath != null) && (! params.ownershipFilePath.isEmpty())) {
					ri.readGlobalOwnership(params.ownershipFilePath);			
				}
			}			
			return ri;
		}		
		return null;
	}
	
	protected VistaPackages getAllVistaPackages(RepositoryInfo ri) {
		List<VistaPackage> packages = ri.getAllPackages(this.params.packageExceptions);
		VistaPackages packageNodes = new VistaPackages(packages);
		return packageNodes;				
	}
	
	public VistaPackages getVistaPackages(RepositoryInfo ri)  {
		List<VistaPackage> packages = null; 
		if (this.params.packages.size() == 0) {
			packages = ri.getAllPackages(this.params.packageExceptions);
			if ((packages == null) || (packages.size() == 0)) {
				MRALogger.logError("Error loading package information from the repository.");
				return null;								
			}
		} else {
			packages = ri.getPackages(this.params.packages);
			if (packages.size() != this.params.packages.size()) {
				MRALogger.logError("Invalid package specification.");
				return null;				
			}
		}
		VistaPackages packageNodes = new VistaPackages(packages);
		return packageNodes;		
	}

	protected FileWrapper getOutputFile() {
		if ((this.params.outputFile == null) || this.params.outputFile.isEmpty()) {
			MRALogger.logError("File " + this.params.outputFile + " is not found");
			return null;
		}
		return new FileWrapper(this.params.outputFile);
	}
	
	protected void writeEntries(FileWrapper fr, List<ToolResult> resultList) {
		if (fr.start()) {
			TerminalFormatter tf = new TerminalFormatter();
			tf.setTab(12);
			for (ToolResult result : resultList) {
				result.write(fr, tf);
			}
			fr.stop();
		}
	}
	
	protected List<String> getEntriesInString() {
		if (this.params.inputFile != null) {
			try {
				Path path = Paths.get(this.params.inputFile);
				Scanner scanner = new Scanner(path);
				List<String> result = new ArrayList<String>();
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					result.add(line);
				}		
				scanner.close();
				return result;
			} catch (IOException e) {
				MRALogger.logError("Unable to open file " + this.params.inputFile);
				return null;
			}
		} else {
			return this.params.entries;
		}			
	}
	
	protected List<EntryId> getEntries() {
		List<String> entriesInString = this.getEntriesInString();
		if (entriesInString != null) {
			List<EntryId> result = new ArrayList<EntryId>(entriesInString.size());
			for (String entryInString : entriesInString) {
				EntryId entryId = EntryId.getInstance(entryInString);
				result.add(entryId);
			}
			return result;
		}
		return null;
	}

	private int getFanoutFlag() {
		try {
			int result = Integer.parseInt(this.params.flag);
			if (result < 0) return 0;
			if (result > 4) return 4;
			return result;
		} catch(Throwable t) {
		}
		return 0;
	}
	
	protected Filter<EntryId> getFilter(RepositoryInfo ri, EntryId entryId) {
		int flag = this.getFanoutFlag();
		switch (flag) {
			case 1: {
				String routineName = entryId.getRoutineName();
				VistaPackage vp = ri.getPackageFromRoutineName(routineName);
				return new ExcludeFilemanCallFanoutFilter(ri, vp.getDefaultPrefix());
			}
			case 2: {
				String routineName = entryId.getRoutineName();
				if (routineName == null) {
					return new ExcludeNonPkgCallFanoutFilter(ri, null);
				} else {
					VistaPackage vp = ri.getPackageFromRoutineName(routineName);
					return new ExcludeNonPkgCallFanoutFilter(ri, vp.getDefaultPrefix());
				}
			}
			case 3:
				return new ExcludeNonRtnFanoutFilter(entryId);
			case 4:
				return new ExcludeAllFanoutFilter();
			default:
				return new PercentRoutineFanoutFilter();
		}
	}
}
