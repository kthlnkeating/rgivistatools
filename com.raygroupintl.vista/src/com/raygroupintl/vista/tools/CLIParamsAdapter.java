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

import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.output.SystemTerminal;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.vista.repository.RepositoryInfo;

public class CLIParamsAdapter {
	public static RepositoryInfo getRepositoryInfo(CLIParams params) {
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
	
	public static FileWrapper getOutputFile(CLIParams params) {
		if ((params.outputFile == null) || params.outputFile.isEmpty()) {
			MRALogger.logError("File " + params.outputFile + " is not found");
			return null;
		} else {
			return new FileWrapper(params.outputFile);
		}
	}
	
	public static Terminal getTerminal(CLIParams params) {
		if ((params.outputFile == null) || params.outputFile.isEmpty()) {
			return new SystemTerminal();
		} else {
			return new FileWrapper(params.outputFile);
		}
	}
}
