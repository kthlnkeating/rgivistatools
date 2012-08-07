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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.util.CLIParamMgr;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.VistaPackages;
import com.raygroupintl.vista.repository.visitor.FanoutWriter;

public class RunTypes {
	private static Map<String, RunType> RUN_TYPES; 
		
	private static class Fanout extends RunType {		
		public Fanout(CLIParams params) {
			super(params);
		}
		
		@Override
		public void run() {
			FileWrapper fr = this.getOutputFile();
			if (fr != null) {
				RepositoryInfo ri = this.getRepositoryInfo();
				if (ri != null) {
					VistaPackages vps = this.getVistaPackages(ri);
					if (vps != null) {						
						FanoutWriter fow = new FanoutWriter(fr, ri);
						vps.accept(fow);
					}
				}
			}
		}
	}
		
	private static Map<String, RunType> getRunTypes(CLIParams params) {
		if (RunTypes.RUN_TYPES == null) {
			RunTypes.RUN_TYPES = new HashMap<String, RunType>();
			RUN_TYPES.put("fanout", new Fanout(params));
		}
		return RunTypes.RUN_TYPES;
	}
	
	private static String getRunTypeListMsg(Map<String, RunType> types) {
		String result = "";
		for (String runType : types.keySet()) {
			if (! result.isEmpty()) {
				result += ", ";
			}
			result += runType;
		}
		return "Possible run types: " + result;
	}
	
	public static RunType getRunType(String[] args) {
		try {
			CLIParams params = CLIParamMgr.parse(CLIParams.class, args);
			Map<String, RunType> runTypes = RunTypes.getRunTypes(params);			
			List<String> positionals = params.positionals;
			if (positionals.size() == 0) {				
				MRALogger.logError("A run type needs to be specified as the first positional argument.\n" + RunTypes.getRunTypeListMsg(runTypes));
				return null;				
			}
			String specifiedType = positionals.get(0);
			RunType specifiedRunType = runTypes.get(specifiedType);
			if (specifiedRunType == null) {
				MRALogger.logError("Specified run type " + specifiedType + " is not known.\n" + RunTypes.getRunTypeListMsg(runTypes));
				return null;
			}
			return specifiedRunType;			
		} catch (Throwable t) {
			MRALogger.logError("Invalid command line options.", t);
			return null;
		}
	}	
}
