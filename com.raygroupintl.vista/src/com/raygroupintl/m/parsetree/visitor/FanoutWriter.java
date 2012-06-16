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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.m.parsetree.Fanout;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.RoutinePackage;
import com.raygroupintl.m.parsetree.RoutinePackages;
import com.raygroupintl.m.parsetree.filter.LocalFanoutFilter;
import com.raygroupintl.m.parsetree.filter.PackageFanoutFilter;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.m.token.MRoutine;
import com.raygroupintl.struct.AndFilter;
import com.raygroupintl.struct.Filter;

public class FanoutWriter extends FanoutRecorder {
	private final static Logger LOGGER = Logger.getLogger(FanoutWriter.class.getName());
	
	private String outputFileName;

	private FileOutputStream os;
	private String eol = MRoutine.getEOL();
	private int packageCount;
	private Filter<Fanout> filter;
	
	public FanoutWriter(String outputFileName) {
		this.outputFileName = outputFileName;
	}
		
	protected void visitRoutinePackage(RoutinePackage routinePackage) {
		try {
			Filter<Fanout> localFilter = new LocalFanoutFilter();
			Filter<Fanout> packageFilter = new PackageFanoutFilter(routinePackage);
			Filter<Fanout> overallFilter = new AndFilter<Fanout>(localFilter, packageFilter);
			this.filter = overallFilter;
			String line = "--------------------------------------------------------------" + this.eol + this.eol;
			this.os.write(line.getBytes());
			++this.packageCount;
			this.os.write((String.valueOf(this.packageCount) + ". " + routinePackage.getPackageName() + this.eol + this.eol).getBytes());
			this.setFilter(this.filter);

			super.visitRoutinePackage(routinePackage);
			
			this.os.write(line.getBytes());
			this.os.flush();
			this.filter = null;
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, "IO Exception");
		}
	}

	public void visitRoutine(Routine routine) {
		try {
			super.visitRoutine(routine);
			Map<LineLocation, List<Fanout>> fanouts = this.getRoutineFanouts();
			if (fanouts != null) {
				Set<Fanout> result = new HashSet<Fanout>();
				for (List<Fanout> fs : fanouts.values()) {
					result.addAll(fs);
				}
				for (Fanout fout : result) {
					String info = fout.toString();
					info += " (" + routine.getKey() + ")";
					this.os.write(("    "  + info + this.eol).getBytes());
				}
			}
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, "Unable to write");
		}
	}
	
	protected void visitRoutinePackages(RoutinePackages rps) {
		try {
			File file = new File(this.outputFileName);
			this.os = new FileOutputStream(file);		
			rps.acceptSubNodes(this);
			this.os.close();
			this.os = null;
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, "Unable to write");			
		}			
	}
}
