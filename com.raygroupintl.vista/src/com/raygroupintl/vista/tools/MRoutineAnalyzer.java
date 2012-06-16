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
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.m.parsetree.ErrorNode;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.RoutineFactory;
import com.raygroupintl.m.parsetree.visitor.ErrorWriter;
import com.raygroupintl.m.parsetree.visitor.FanoutWriter;
import com.raygroupintl.m.struct.MError;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFRoutine;
import com.raygroupintl.m.token.MRoutine;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.annotation.ParseException;
import com.raygroupintl.vista.repository.RepositoryInfo;

public class MRoutineAnalyzer {
	private final static Logger LOGGER = Logger.getLogger(MRoutineAnalyzer.class.getName());

	private static class MRARoutineFactory implements RoutineFactory {
		private TFRoutine tokenFactory;
		
		public MRARoutineFactory(TFRoutine tokenFactory) {
			this.tokenFactory = tokenFactory;
		}
		
		@Override
		public String getName(Path path) {
			String fileName = path.getFileName().toString();
			String name = fileName.split(".m")[0];
			return name;
		}
		
		@Override
		public Node getNode(Path path) {
			try {
				MRoutine mr = this.tokenFactory.tokenize(path);
				Routine node = mr.getNode();
				return node;
			} catch (SyntaxErrorException e) {
				return new ErrorNode(MError.ERR_BLOCK_STRUCTURE);
			} catch (IOException e) {
				return new ErrorNode(MError.ERR_ROUTINE_PATH);
			}
		}	
		
		public static MRARoutineFactory getInstance(MVersion version) {
			try {
				MTFSupply supply = MTFSupply.getInstance(version);
				TFRoutine tf = new TFRoutine(supply);
				return new MRARoutineFactory(tf);
			} catch (ParseException e) {
				LOGGER.log(Level.SEVERE, "Unable to load M parser definitions.");
				return null;
			}
		}
	}
	
	public void writeErrors(CLIParams options, RoutineFactory rf) throws IOException, SyntaxErrorException {		
		String outputFile = options.outputFile; 
		ErrorExemptions exemptions = ErrorExemptions.getVistAFOIAInstance();
		ErrorWriter ew = new ErrorWriter(exemptions);
		ew.write(outputFile, rf);
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
			String at = options.analysisType;
			if (at.equalsIgnoreCase("error")) {
				MRARoutineFactory rf = MRARoutineFactory.getInstance(MVersion.CACHE);
				m.writeErrors(options, rf);
				return;
			}
			if (at.equalsIgnoreCase("fanout")) {
				//MRARoutineFactory rf = MRARoutineFactory.getInstance(MVersion.CACHE);
				MTFSupply supply = MTFSupply.getInstance(MVersion.CACHE);
				TFRoutine tf = new TFRoutine(supply);
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
