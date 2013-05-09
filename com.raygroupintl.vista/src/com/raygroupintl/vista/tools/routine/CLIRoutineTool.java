package com.raygroupintl.vista.tools.routine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
//---------------------------------------------------------------------------
//Copyright 2012 Ray Group International
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//---------------------------------------------------------------------------

import java.util.List;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.vista.tools.CLIParams;
import com.raygroupintl.vista.tools.MRALogger;
import com.raygroupintl.vista.tools.MRARoutineFactory;
import com.raygroupintl.vista.tools.Tool;

abstract class CLIRoutineTool extends Tool {
	public CLIRoutineTool(CLIParams params) {
		super(params);
	}
	
	private List<Path> getMFiles() {
		List<Path> absoluteFileNames = new ArrayList<Path>();
		for (String mf : this.params.additionalMFiles) {
			Path path = Paths.get(mf);
			String fileName = path.getFileName().toString();
			if (fileName.indexOf('.') < 0) {
				fileName = fileName + ".m";
			}
			Path parent = path.getParent();
			if (parent != null) {
				path = Paths.get(parent.toString(), fileName);
			} else {
				path = Paths.get(fileName);
			}
			if (! path.isAbsolute()) {
				String cwd = System.getProperty("user.dir");
				path = Paths.get(cwd, path.toString());
			}
			if (Files.exists(path)) {
				absoluteFileNames.add(path);
			} else {
				MRALogger.logError("Cannot find file: " + path.toString());					
			}
		}
		if (absoluteFileNames.size() > 0) {				
			return absoluteFileNames;
		} else {
			MRALogger.logError("No file specified or none of the specified files exists.");
			return null;
		}
	}
	
	protected List<Routine> getRoutines() {
		MRARoutineFactory rf = MRARoutineFactory.getInstance(MVersion.CACHE);
		if (rf != null) {
			List<Path> paths = this.getMFiles();
			if (paths != null) {
				List<Routine> routines = new ArrayList<Routine>();
				for (Path p : paths) {
					Routine routine = rf.getRoutineNode(p);
					routines.add(routine);
				}
				if (routines.size() > 0) {
					return routines;
				}
			}
		}
		return null;
	}		
}