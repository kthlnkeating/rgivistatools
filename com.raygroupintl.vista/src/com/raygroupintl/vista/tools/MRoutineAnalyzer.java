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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.m.parsetree.ErrorNode;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.SerializedBlocksSupply;
import com.raygroupintl.m.struct.MError;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFRoutine;
import com.raygroupintl.m.token.MRoutine;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.annotation.ParseException;
import com.raygroupintl.stringlib.EndsWithFilter;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.NameWithIndices;
import com.raygroupintl.vista.repository.FileSupply;
import com.raygroupintl.vista.repository.MGlobalNode;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.RoutineFactory;
import com.raygroupintl.vista.repository.VistaPackages;
import com.raygroupintl.vista.repository.VistaPackage;
import com.raygroupintl.vista.repository.visitor.APIOverallRecorder;
import com.raygroupintl.vista.repository.visitor.APIWriter;
import com.raygroupintl.vista.repository.visitor.EntryWriter;
import com.raygroupintl.vista.repository.visitor.ErrorWriter;
import com.raygroupintl.vista.repository.visitor.FaninWriter;
import com.raygroupintl.vista.repository.visitor.FanoutWriter;
import com.raygroupintl.vista.repository.visitor.SerializedRoutineWriter;

public class MRoutineAnalyzer {
	private final static Logger LOGGER = Logger.getLogger(MRoutineAnalyzer.class.getName());

	private static Map<String, String> replacementRoutines = new HashMap<String, String>();
	static {
		replacementRoutines.put("%ZOSV", "ZOSVONT");
		replacementRoutines.put("%ZIS4", "ZIS4ONT");
		replacementRoutines.put("%ZISF", "ZISFONT");
		replacementRoutines.put("%ZISH", "ZISHONT");
		replacementRoutines.put("%XUCI", "ZISHONT");

		replacementRoutines.put("%ZISTCPS", "ZISTCPS");
		replacementRoutines.put("%ZTMDCL", "ZTMDCL");
		
		replacementRoutines.put("%ZOSVKR", "ZOSVKRO");
		replacementRoutines.put("%ZOSVKSE", "ZOSVKSOE");
		replacementRoutines.put("%ZOSVKSS", "ZOSVKSOS");
		replacementRoutines.put("%ZOSVKSD", "ZOSVKSD");

		replacementRoutines.put("%ZTLOAD", "ZTLOAD");
		replacementRoutines.put("%ZTLOAD1", "ZTLOAD1");
		replacementRoutines.put("%ZTLOAD2", "ZTLOAD2");
		replacementRoutines.put("%ZTLOAD3", "ZTLOAD3");
		replacementRoutines.put("%ZTLOAD4", "ZTLOAD4");
		replacementRoutines.put("%ZTLOAD5", "ZTLOAD5");
		replacementRoutines.put("%ZTLOAD6", "ZTLOAD6");
		replacementRoutines.put("%ZTLOAD7", "ZTLOAD7");
		
		replacementRoutines.put("%ZTM", "ZTM");
		replacementRoutines.put("%ZTM0", "ZTM0");
		replacementRoutines.put("%ZTM1", "ZTM1");
		replacementRoutines.put("%ZTM2", "ZTM2");
		replacementRoutines.put("%ZTM3", "ZTM3");
		replacementRoutines.put("%ZTM4", "ZTM4");
		replacementRoutines.put("%ZTM5", "ZTM5");
		replacementRoutines.put("%ZTM6", "ZTM6");
		
		replacementRoutines.put("%ZTMS", "ZTMS");
		replacementRoutines.put("%ZTMS0", "ZTMS0");
		replacementRoutines.put("%ZTMS1", "ZTMS1");
		replacementRoutines.put("%ZTMS2", "ZTMS2");
		replacementRoutines.put("%ZTMS3", "ZTMS3");
		replacementRoutines.put("%ZTMS4", "ZTMS4");
		replacementRoutines.put("%ZTMS5", "ZTMS5");
		replacementRoutines.put("%ZTMS7", "ZTMS7");
		replacementRoutines.put("%ZTMSH", "ZTMSH");

		replacementRoutines.put("%DT", "DIDT");
		replacementRoutines.put("%DTC", "DIDTC");
		replacementRoutines.put("%RCR", "DIRCR");

		replacementRoutines.put("%ZTER", "ZTER");
		replacementRoutines.put("%ZTER1", "ZTER1");

		replacementRoutines.put("%ZTPP", "ZTPP");
		replacementRoutines.put("%ZTP1", "ZTP1");
		replacementRoutines.put("%ZTPTCH", "ZTPTCH");
		replacementRoutines.put("%ZTRDE", "ZTRDE");
		replacementRoutines.put("%ZTMOVE", "ZTMOVE");
		
		replacementRoutines.put("%ZIS", "ZIS");
		replacementRoutines.put("%ZIS1", "ZIS1");
		replacementRoutines.put("%ZIS2", "ZIS2");
		replacementRoutines.put("%ZIS3", "ZIS3");
		replacementRoutines.put("%ZIS5", "ZIS5");
		replacementRoutines.put("%ZIS6", "ZIS6");
		replacementRoutines.put("%ZIS7", "ZIS7");
		replacementRoutines.put("%ZISC", "ZISC");
		replacementRoutines.put("%ZISP", "ZISP");
		replacementRoutines.put("%ZISS", "ZISS");
		replacementRoutines.put("%ZISS1", "ZISS1");
		replacementRoutines.put("%ZISS2", "ZISS2");
		replacementRoutines.put("%ZISTCP", "ZISTCP");
		replacementRoutines.put("%ZISUTL", "ZISUTL");
	}
	
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
	
	private static class PercentRoutineFilter implements Filter<EntryId> {
		@Override
		public boolean isValid(EntryId input) {
			String routineName = input.getRoutineName();
			if ((routineName != null) && (! routineName.isEmpty())) {
				char ch = routineName.charAt(0);
				return ch != '%';
			}
			return true;
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
	
	// -t fanin -o "C:\Afsin\Dependency Tool Verification\java\fanin.dat"
	// -t api -i "C:\Afsin\Dependency Tool Verification\java\fanin.dat" -o "C:\Afsin\Dependency Tool Verification\java\api.dat"
	// -t glb -o "C:\Afsin\Dependency Tool Verification\glb.dat
	// -t apisingle -i "C:\Afsin\Dependency Tool Verification\serial" -o "C:\Afsin\Dependency Tool Verification\java\api_single.dat"
	// -t apisingle -i "C:\Afsin\Dependency Tool Verification\serial" -o "C:\Afsin\Dependency Tool Verification\java\api_single.dat" -e ADD^ABSV88B -e DEL^ABSV88B -e VENTRY^DIEFU		
	public static void main(String[] args) {
		try {
			CLIParams options = CLIParams.getInstance(args);
			if (options == null) return;
			
			MRoutineAnalyzer m = new MRoutineAnalyzer();
			MRARoutineFactory rf = MRARoutineFactory.getInstance(MVersion.CACHE);
			RepositoryInfo ri = RepositoryInfo.getInstance(rf);
			VistaPackages packageNodes = m.getRoutinePackages(options, ri);
			String outputPath = options.outputFile;
			String at = options.analysisType;
			if (at.equalsIgnoreCase("error")) {
				FileWrapper fr = new FileWrapper(outputPath);
				ErrorExemptions exemptions = ErrorExemptions.getVistAFOIAInstance();
				ErrorWriter ew = new ErrorWriter(exemptions, fr);
				packageNodes.accept(ew);
				return;
			}
			if (at.equalsIgnoreCase("fanout")) {
				FileWrapper fr = new FileWrapper(outputPath);
				FanoutWriter fow = new FanoutWriter(fr);
				packageNodes.accept(fow);
				return;
			}
			if (at.equalsIgnoreCase("fanin")) {
				FileWrapper fr = new FileWrapper(outputPath);
				FaninWriter fiw = new FaninWriter(ri, fr);
				packageNodes.accept(fiw);
				return;
			}
			if (at.equalsIgnoreCase("entry")) {
				FileWrapper fr = new FileWrapper(outputPath);
				EntryWriter ew = new EntryWriter(fr);
				for (String r : options.routines) {
					ew.addRoutineNameFilter(r);
				}
				packageNodes.accept(ew);
				return;
			}
			if (at.equalsIgnoreCase("api")) {
				FileWrapper fr = new FileWrapper(outputPath);
				APIOverallRecorder api = new APIOverallRecorder();
				packageNodes.accept(api);
				BlocksSupply blocks = api.getBlocks();
				APIWriter apiw = new APIWriter(fr, blocks, replacementRoutines);
				PercentRoutineFilter filter = new PercentRoutineFilter();
				apiw.setFilter(filter);
				apiw.write(options.inputFile);
				return;
			}
			if (at.equalsIgnoreCase("apis")) {
				FileWrapper fr = new FileWrapper(outputPath);
				BlocksSupply blocks = new SerializedBlocksSupply(options.parseTreeDirectory);
				APIWriter apiw = new APIWriter(fr, blocks, replacementRoutines);
				PercentRoutineFilter filter = new PercentRoutineFilter();
				apiw.setFilter(filter);
				if (options.inputFile != null) {
					apiw.write(options.inputFile);					
				} else {
					apiw.writeEntries(options.entries);
				}
				return;
			}
			if (at.equalsIgnoreCase("glb")) {
				MGlobalNode root = new MGlobalNode();
				FileSupply fs = new FileSupply();
				List<Path> paths = fs.getFiles(new EndsWithFilter(".zwr"));
				Filter<NameWithIndices> niFilter = new Filter<NameWithIndices>() {
					@Override
					public boolean isValid(NameWithIndices input) {
						if (! input.getName().equals("DIC")) return false;
						String[] indices = input.getIndices();
						if (indices.length != 3) return false;
						if (! indices[1].equals("0")) return false;
						if (! indices[2].equals("\"GL\"")) return false;
						return true;
					}
				};				
				root.read(paths, niFilter);
				LOGGER.log(Level.INFO, "Done");			
				return;
			}
			if (at.equalsIgnoreCase("serial")) {
				SerializedRoutineWriter srw = new SerializedRoutineWriter(options.parseTreeDirectory);
				packageNodes.accept(srw);
				return;
			}
						
			LOGGER.log(Level.SEVERE, "Unknown analysis type " + at);
		} catch (Throwable t) {
			LOGGER.log(Level.SEVERE, "Unexpected error", t);
			return;
		}
	}
}
