package com.raygroupintl.vista.tools.entry;

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.m.struct.CodeLocation;
import com.raygroupintl.m.tool.entry.MEntryToolInput;
import com.raygroupintl.m.tool.entry.MEntryToolResult;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariables;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariablesTool;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariablesToolParams;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.vista.tools.CLIParams;
import com.raygroupintl.vista.tools.CLIParamsAdapter;

public class CLIAssumedVariablesTool extends CLIEntryTool<AssumedVariables> {	
	private boolean showDetail = false;
	
	public CLIAssumedVariablesTool(CLIParams params) {
		super(params);
		this.showDetail = CLIParamsAdapter.toOutputFlags(params).getShowDetail(false);
	}
	
	@Override
	protected boolean isEmpty(AssumedVariables assumedVariables) {
		return assumedVariables.isEmpty();
	}
			
	@Override
	protected MEntryToolResult<AssumedVariables> getResult(MEntryToolInput input) {
		AssumedVariablesToolParams params = CLIETParamsAdapter.toAssumedVariablesToolParams(this.params);
		AssumedVariablesTool a = new AssumedVariablesTool(params);
		return a.getResult(input);			
	}
	
	@Override
	protected void write(AssumedVariables result, Terminal t, TerminalFormatter tf) {
		if (this.showDetail) {
			List<String> tbw = new ArrayList<String>();
			for (String name : result.toSet()) {
				CodeLocation location = result.get(name);
				String out = name + "->" + location.toString();
				tbw.add(out);
			}
			t.writeSortedFormatted("ASSUMED", tbw, tf);
		} else {
			t.writeSortedFormatted("ASSUMED", result.toSet(), tf);
		}
	}
}
