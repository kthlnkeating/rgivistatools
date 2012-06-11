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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.m.parsetree.Fanout;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.filter.LocalFanoutFilter;
import com.raygroupintl.m.parsetree.filter.PackageFanoutFilter;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.m.token.MRoutine;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.struct.AndFilter;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.vista.repository.RepositoryInfo;

public class FanoutWriter  {
	private final static Logger LOGGER = Logger.getLogger(FanoutWriter.class.getName());

	private FileOutputStream os;
	private String eol = MRoutine.getEOL();
	private int packageCount;
	private Filter<Fanout> filter;
	
	protected void visitRoutinePackage(RepositoryInfo.PackageInRepository routinePackage) {
		try {
			Filter<Fanout> localFilter = new LocalFanoutFilter();
			Filter<Fanout> packageFilter = new PackageFanoutFilter(routinePackage);
			Filter<Fanout> overallFilter = new AndFilter<Fanout>(localFilter, packageFilter);
			this.filter = overallFilter;
			String line = "--------------------------------------------------------------" + this.eol + this.eol;
			this.os.write(line.getBytes());
			++this.packageCount;
			this.os.write((String.valueOf(this.packageCount) + ". " + routinePackage.getPackageName() + this.eol + this.eol).getBytes());
			Set<String> existing = new HashSet<String>();
			try {
				List<Path> paths = routinePackage.getRoutineFilePaths();
				for (Path path : paths) {
					try {
						MRoutine tr = routinePackage.tokenizer.tokenize(path);
						Routine node = tr.getNode();
						this.visitRoutine(node, existing);
					} catch (SyntaxErrorException ex) {
						LOGGER.log(Level.SEVERE, "Syntax error found in routine: " + path.toString());
					} 
				}
			} catch (IOException ioex) {
				LOGGER.log(Level.SEVERE, "IO error for package: " + routinePackage.getPackageName());
			}				
			this.os.write(line.getBytes());
			this.os.flush();
			this.filter = null;
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, "IO Exception");
		}
	}

	public void visitRoutine(Routine routine, Set<String> existing) {
		try {
			FanoutRecorder forer = new FanoutRecorder();
			forer.setFilter(this.filter);
			routine.accept(forer);
			Map<LineLocation, List<Fanout>> fanouts = forer.getRoutineFanouts();
			for (List<Fanout> fouts : fanouts.values()) {	
				for (Fanout fout : fouts) {
					String info = fout.toString();
					if (existing.contains(info)) {
						continue;
					}
					existing.add(info);
					info += " (" + routine.getKey() + ")";
					this.os.write(("    "  + info + this.eol).getBytes());
				}
			}	
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, "Unable to write");
		}
	}

	public void write(String outputName, List<RepositoryInfo.PackageInRepository> packages) throws IOException {
		File file = new File(outputName);
		this.os = new FileOutputStream(file);
		for (RepositoryInfo.PackageInRepository p : packages) {
			this.visitRoutinePackage(p);
		}
		this.os.close();
		this.os = null;
	}	
}
