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

package com.raygroupintl.vista.tools.entry;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.tool.CommonToolParams;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.entry.MEntryToolInput;
import com.raygroupintl.m.tool.entry.RecursionDepth;
import com.raygroupintl.m.tool.entry.RecursionSpecification;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariablesToolParams;
import com.raygroupintl.m.tool.entry.basiccodeinfo.BasicCodeInfoToolParams;
import com.raygroupintl.m.tool.entry.localassignment.LocalAssignmentToolParams;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.tools.CLIParams;
import com.raygroupintl.vista.tools.CLIParamsAdapter;
import com.raygroupintl.vista.tools.MRALogger;

class CLIETParamsAdapter {
	public static RecursionSpecification toRecursionSpecification(CLIParams params) {
		String rdName = params.recursionDepth;
		if ((rdName == null) || (rdName.isEmpty())) {
			rdName = "label";
		}
		RecursionSpecification rs = new RecursionSpecification();
		try {
			RecursionDepth d = RecursionDepth.get(rdName);
			rs.setDepth(d);
			rs.addIncludedFanoutNamespaces(params.includeNamespaces);
			rs.addExcludedFanoutNamespaces(params.excludeNamespaces);
			rs.addExcludedFanoutExceptionNamespaces(params.excludeExceptionNamespaces);
			return rs;
		} catch (Exception ex) {
			return null;
		}			
	}
	
	public static CommonToolParams toCommonToolParams(CLIParams params) {
		ParseTreeSupply pts = CLIParamsAdapter.getParseTreeSupply(params);
		CommonToolParams result = new CommonToolParams(pts);
		RecursionSpecification rs = toRecursionSpecification(params);
		result.setRecursionSpecification(rs);	
		return result;				
	}

	public static LocalAssignmentToolParams toLocalAssignmentToolParams(CLIParams params) {
		ParseTreeSupply pts = CLIParamsAdapter.getParseTreeSupply(params);
		LocalAssignmentToolParams result = new LocalAssignmentToolParams(pts);
		if (params.includes.size() > 0) {
			result.addLocals(params.includes);
		}
		RecursionSpecification rs = toRecursionSpecification(params);
		result.setRecursionSpecification(rs);	
		return result;		
	}

	public static AssumedVariablesToolParams toAssumedVariablesToolParams(CLIParams params) {
		ParseTreeSupply pts = CLIParamsAdapter.getParseTreeSupply(params);
		AssumedVariablesToolParams result = new AssumedVariablesToolParams(pts);
		if (params.excludes.size() > 0) {
			result.addExpected(params.excludes);
		}
		RecursionSpecification rs = toRecursionSpecification(params);
		result.setRecursionSpecification(rs);	
		return result;		
	}

	public static BasicCodeInfoToolParams toBasicCodeInfoToolParams(CLIParams params, RepositoryInfo repositoryInfo) {
		ParseTreeSupply pts = CLIParamsAdapter.getParseTreeSupply(params);
		BasicCodeInfoToolParams result = new BasicCodeInfoToolParams(pts, repositoryInfo);

		RecursionSpecification rs = toRecursionSpecification(params);
		result.setRecursionSpecification(rs);	
		return result;		
	}
	
	protected static List<String> getEntriesInString(CLIParams params) {
		if (params.inputFile != null) {
			try {
				Path path = Paths.get(params.inputFile);
				Scanner scanner = new Scanner(path);
				List<String> result = new ArrayList<String>();
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					result.add(line);
				}		
				scanner.close();
				return result;
			} catch (IOException e) {
				MRALogger.logError("Unable to open file " + params.inputFile);
				return null;
			}
		} else {
			return params.entries;
		}			
	}
	
	public static List<EntryId> getEntries(CLIParams params) {
		List<String> entriesInString = getEntriesInString(params);
		if (entriesInString != null) {
			List<EntryId> result = new ArrayList<EntryId>(entriesInString.size());
			for (String entryInString : entriesInString) {
				EntryId entryId = EntryId.getInstance(entryInString);
				result.add(entryId);
			}
			return result;
		}
		return null;
	}
	
	private static List<String> getCLIRoutines(CLIParams params) {
		List<String> specifiedRoutineNames = params.routines;
		boolean hasRegularExpression = false;
		Pattern p = Pattern.compile("[^a-zA-Z0-9\\%]");
		for (String routineName : specifiedRoutineNames) {
			hasRegularExpression = p.matcher(routineName).find();		
			if (hasRegularExpression) break;
		}
		if (hasRegularExpression) {
			List<String> expandedRoutineNames = new ArrayList<String>();
			ParseTreeSupply pts = CLIParamsAdapter.getParseTreeSupply(params);
			Collection<String> allRoutineNames = pts.getAllRoutineNames();
			boolean[] matched = new boolean[specifiedRoutineNames.size()];
			for (String routineName : allRoutineNames) {
				int index = 0;
				for (String specifiedRoutineName : specifiedRoutineNames) {
					if (routineName.matches(specifiedRoutineName)) {
						expandedRoutineNames.add(routineName);
						matched[index] = true;
					}
					++index;
				}				
			}
			for (int i=0; i<matched.length; ++i) {
				if (! matched[i]) {
					MRALogger.logError("No match is found for routine name: " + specifiedRoutineNames.get(i));					
				}
			}			
			return expandedRoutineNames;
		} else {
			return specifiedRoutineNames;
		}
	}
	
	public static MEntryToolInput getMEntryToolInput(CLIParams params) {
		MEntryToolInput input = new MEntryToolInput();
		input.addRoutines(CLIETParamsAdapter.getCLIRoutines(params));
		List<EntryId> entryIds = getEntries(params);
		input.addEntries(entryIds);
		return input;
	}
}
