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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.m.parsetree.visitor.ErrorRecorder;
import com.raygroupintl.m.parsetree.visitor.FanoutWriter;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.m.struct.MError;
import com.raygroupintl.m.struct.ObjectInRoutine;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFRoutine;
import com.raygroupintl.m.token.TRoutine;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.vista.repository.FileSupply;
import com.raygroupintl.vista.repository.RepositoryInfo;

public class MRoutineAnalyzer {
	private final static Logger LOGGER = Logger.getLogger(MRoutineAnalyzer.class.getName());

	public void writeErrors(final TFRoutine tf, final ErrorExemptions exemptions, String outputPath) throws IOException, SyntaxErrorException {
		int errorCount = 0;
		final File file = new File(outputPath);
		final FileOutputStream os = new FileOutputStream(file);
		final String eol = TRoutine.getEOL();
		List<Path> paths = FileSupply.getAllMFiles();
		for (Path path : paths) {
			TRoutine r = tf.tokenize(path);			
			final String name = r.getName();
			if (! exemptions.containsRoutine(name)) {
				Set<LineLocation> locations = exemptions.getLines(name);
				ErrorRecorder ev = new ErrorRecorder(locations);
				List<ObjectInRoutine<MError>> errors = ev.visitErrors(r.getNode());
				if (errors.size() > 0) {
					os.write((eol + eol + r.getName() + eol).getBytes());
					errorCount += errors.size();
					LineLocation lastLocation = new LineLocation("", 0);
					for (ObjectInRoutine<MError> error: errors) {
						if (! error.getLocation().equals(lastLocation)) {
							lastLocation = error.getLocation();
							String offset = (lastLocation.getOffset() == 0 ? "" : '+' + String.valueOf(lastLocation.getOffset()));
							os.write(("  " + lastLocation.getTag() + offset + eol).getBytes());
						}
						os.write(("    " + error.getObject().getText() + eol).getBytes());
					}
					errorCount += errors.size();
				}
			}			
		}
		os.write((eol + eol + "Number Errors: " + String.valueOf(errorCount) + eol).getBytes());
		os.close();
	}
	
	public void writeErrors(CLIParams options, TFRoutine topToken) throws IOException, SyntaxErrorException {		
		String outputFile = options.outputFile; 
		ErrorExemptions exemptions = ErrorExemptions.getVistAFOIAInstance();
		this.writeErrors(topToken, exemptions, outputFile);				
	}

	public void writeFanout(CLIParams options, TFRoutine topToken) throws IOException, SyntaxErrorException {
		RepositoryInfo ri = RepositoryInfo.getInstance(topToken);
		List<RepositoryInfo.PackageInRepository> packages = null; 
		if (options.packages.size() == 0) {
			packages = ri.getAllPackages();
		} else {
			packages = ri.getPackages(options.packages);
		}
				
		String outputFile = options.outputFile;
		FanoutWriter fow = new FanoutWriter();
		fow.write(outputFile, packages);
	}
	
	public static void main(String[] args) {
		try {
			CLIParams options = CLIParams.getInstance(args);
			if (options == null) return;

			MRoutineAnalyzer m = new MRoutineAnalyzer();
			MTFSupply supply = MTFSupply.getInstance(MVersion.CACHE);
			TFRoutine tf = new TFRoutine(supply);
			String at = options.analysisType;
			if (at.equalsIgnoreCase("error")) {
				m.writeErrors(options, tf);
				return;
			}
			if (at.equalsIgnoreCase("fanout")) {
				m.writeFanout(options, tf);
				return;
			}
			LOGGER.log(Level.SEVERE, "Unknown analysis type " + at);
		} catch (Throwable t) {
			LOGGER.log(Level.SEVERE, "Unexpected error", t);
			return;
		}
	}
}
