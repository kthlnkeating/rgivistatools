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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.m.parsetree.ErrorNode;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.Blocks;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.struct.MError;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFRoutine;
import com.raygroupintl.m.token.MRoutine;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.annotation.ParseException;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.RoutineFactory;
import com.raygroupintl.vista.repository.VistaPackages;
import com.raygroupintl.vista.repository.VistaPackage;
import com.raygroupintl.vista.repository.visitor.APIOverallRecorder;
import com.raygroupintl.vista.repository.visitor.APIWriter;
import com.raygroupintl.vista.repository.visitor.ErrorWriter;
import com.raygroupintl.vista.repository.visitor.FaninWriter;
import com.raygroupintl.vista.repository.visitor.FanoutWriter;

public class MRoutineAnalyzer {
	private final static Logger LOGGER = Logger.getLogger(MRoutineAnalyzer.class.getName());

	public static class MRARoutineFactory implements RoutineFactory {
		private TFRoutine tokenFactory;
		
		public MRARoutineFactory(TFRoutine tokenFactory) {
			this.tokenFactory = tokenFactory;
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
	
	private VistaPackages getRoutinePackages(CLIParams options, RepositoryInfo ri)  throws IOException, SyntaxErrorException {
		List<VistaPackage> packages = null; 
		if (options.packages.size() == 0) {
			packages = ri.getAllPackages();
		} else {
			packages = ri.getPackages(options.packages);
		}
		VistaPackages packageNodes = new VistaPackages(packages);
		return packageNodes;		
	}
	
	private static List<EntryId> getEntryIds() {
		List<EntryId> result = new ArrayList<EntryId>();
		EntryId eid0 = new EntryId("SCDXMSG2", "LATEACT");
		result.add(eid0);
		EntryId eid1 = new EntryId("DGUTL3", "GETSHAD");
		result.add(eid1);
		EntryId eid2 = new EntryId("SCDXUTL", "FMDATE");
		result.add(eid2);
		EntryId eid3 = new EntryId("LRPXAPIU", "ABDN");
		result.add(eid3);
		EntryId eid4 = new EntryId("SCDXPOV", "EN");
		result.add(eid4);
		return result;
	}
	
	public static void main(String[] args) {
		try {
			CLIParams options = CLIParams.getInstance(args);
			if (options == null) return;

			MRoutineAnalyzer m = new MRoutineAnalyzer();
			MRARoutineFactory rf = MRARoutineFactory.getInstance(MVersion.CACHE);
			RepositoryInfo ri = RepositoryInfo.getInstance(rf);
			VistaPackages packageNodes = m.getRoutinePackages(options, ri);
			String outputFile = options.outputFile;
			FileWrapper fr = new FileWrapper(outputFile);
			String at = options.analysisType;
			if (at.equalsIgnoreCase("error")) {
				ErrorExemptions exemptions = ErrorExemptions.getVistAFOIAInstance();
				ErrorWriter ew = new ErrorWriter(exemptions, fr);
				packageNodes.accept(ew);
				return;
			}
			if (at.equalsIgnoreCase("fanout")) {
				FanoutWriter fow = new FanoutWriter(fr);
				packageNodes.accept(fow);
				return;
			}
			if (at.equalsIgnoreCase("fanin")) {
				FaninWriter fiw = new FaninWriter(ri, fr);
				packageNodes.accept(fiw);
				return;
			}
			if (at.equalsIgnoreCase("api")) {
				APIOverallRecorder api = new APIOverallRecorder();
				packageNodes.accept(api);
				Map<String, Blocks> blocks = api.getBlocks();
				APIWriter apiw = new APIWriter(ri, fr, blocks);
				List<EntryId> entryIds = getEntryIds();
				apiw.write(entryIds);
				return;
			}
			LOGGER.log(Level.SEVERE, "Unknown analysis type " + at);
		} catch (Throwable t) {
			LOGGER.log(Level.SEVERE, "Unexpected error", t);
			return;
		}
	}
}
