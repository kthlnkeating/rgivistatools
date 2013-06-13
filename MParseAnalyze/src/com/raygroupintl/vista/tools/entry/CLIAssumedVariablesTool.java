package com.raygroupintl.vista.tools.entry;

import com.raygroupintl.m.tool.entry.MEntryToolInput;
import com.raygroupintl.m.tool.entry.MEntryToolResult;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariables;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariablesTool;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariablesToolParams;
import com.raygroupintl.vista.tools.CLIParams;

public class CLIAssumedVariablesTool extends CLIEntryTool<AssumedVariables> {	
	public CLIAssumedVariablesTool(CLIParams params) {
		super(params);
	}
	
	@Override
	protected MEntryToolResult<AssumedVariables> getResult(MEntryToolInput input) {
		AssumedVariablesToolParams params = CLIETParamsAdapter.toAssumedVariablesToolParams(this.params);
		AssumedVariablesTool a = new AssumedVariablesTool(params);
		return a.getResult(input);			
	}
}
