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

import java.util.Set;
import java.util.TreeSet;

import com.raygroupintl.util.CLIParamMgr;
import com.raygroupintl.vista.tools.entry.RepoEntryTools;
import com.raygroupintl.vista.tools.fnds.ToolErrorException;

public class MRoutineAnalyzer {
	private static CLIParams getCommandLineParamaters(String[] args) {
		try {
			CLIParams params = CLIParamMgr.parse(CLIParams.class, args);
			return params;
		} catch (Throwable t) {
			MRALogger.logError("Invalid command line options.", t);
			return null;
		}		
	}
	
	private static String getOptionMsg(Set<String> options) {
		String result = "";
		for (String option : options) {
			if (! result.isEmpty()) {
				result += ", ";
			}
			result += option;
		}
		return "Possible run types: " + result;
	}

	private static String getRunTypeOptionsMsg(Tools[] rtss) {
		TreeSet<String> allOptions = new TreeSet<String>();
		for (Tools rts : rtss) {
			Set<String > options = rts.getRunTypeOptions();
			allOptions.addAll(options);
		}
		allOptions.add("file");
		return getOptionMsg(allOptions);
	}
	
	private static void logErrorWithOptions(String firstLineMsg, Tools[] rtss) {
		String secondLineMsg = getRunTypeOptionsMsg(rtss);
		MRALogger.logError(firstLineMsg + "\n" + secondLineMsg + "\n");
	}

	private static void logErrorWithOptions(String firstLineMsg, Tools rts) {
		String secondLineMsg = getOptionMsg(rts.getRunTypeOptions());
		MRALogger.logError(firstLineMsg + "\n" + secondLineMsg + "\n");
	}

	private static boolean run(Tools rts, String runTypeOption, CLIParams params) {
		Tool rt = rts.getRunType(runTypeOption, params);
		if (rt != null) {
			MRALogger.logInfo("Started " + runTypeOption + ".");
			rt.run();
			MRALogger.logInfo("Ended " + runTypeOption + ".");
		    return true;
		} else {
			return false;
		}
	}
	
	public static void main(String[] args) {
		try {
			Tools[] rtss = new Tools[]{new RepositoryTools(), new RepoEntryTools(), new MacroTools(), new MFileTools(), new UtilityTools()};
		
			CLIParams params = getCommandLineParamaters(args);	
			if (params == null) return;
			String runTypeOption = params.getPositional(0, null);
			if (runTypeOption == null) {				
				logErrorWithOptions("A run type option needs to be specified as the first positional argument.", rtss);
				return;				
			}
			
			params.popPositional();
			
			if (runTypeOption.equals("file")) {
				if (params.positionals.size() == 0) {
					logErrorWithOptions("A addditional run type option needs to be specified following \"file\".", rtss[2]);
					return;
				}
				runTypeOption = params.positionals.get(0);
				params.popPositional();
				
				if (run(rtss[2], runTypeOption, params)) return;
				logErrorWithOptions("Specified run type option " + runTypeOption + " is not know.", rtss[2]);					
			} else {
				for (Tools rts : rtss) {
					if (run(rts, runTypeOption, params)) return;
				}
				logErrorWithOptions("Specified run type option " + runTypeOption + " is not know.", rtss);					
			}
			
		} catch (ToolErrorException e) {
			MRALogger.logError("Error running tool", e);
		} catch (Throwable t) {
			MRALogger.logError("Unexpected error.", t);
		}
	}
}
