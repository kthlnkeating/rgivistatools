package com.raygroupintl.vista.tools.returntype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.vista.tools.fnds.ToolResult;

public class ReturnTypeResult implements ToolResult {

	private EntryId entryId; //param entry
	private List<ReturnTypeForFanout> fanoutReturnTypes; //something which ties a fanout entryId to a return status.
	
	public ReturnTypeResult(EntryId entryId,
			List<ReturnTypeForFanout> fanoutReturnTypes) {
		super();
		this.entryId = entryId;
		this.fanoutReturnTypes = fanoutReturnTypes;
	}

	public EntryId getEntryId() {
		return entryId;
	}

	public void setEntryId(EntryId entryId) {
		this.entryId = entryId;
	}

	public List<ReturnTypeForFanout> getFanoutReturnTypes() {
		return fanoutReturnTypes;
	}

	public void setFanoutReturnTypes(List<ReturnTypeForFanout> fanoutReturnTypes) {
		this.fanoutReturnTypes = fanoutReturnTypes;
	}

	//TODO: consider moving this to FileWraper so it can print lists with new lines.
	public String padLeft(String s, int n) {
	    return String.format("%1$" + n + "s", s);  
	}
	
	public String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}

	@Override
	public void write(Terminal t, TerminalFormatter tf) {
		t.writeEOL(" " + this.entryId.toString2());
		
		Collection<String> toStrings = new ArrayList<String>();
		for (ReturnTypeForFanout rtff : fanoutReturnTypes)
			toStrings.add(rtff.toString());
		
		//t.writeFormatted("FANOUTS", toStrings, tf);
		t.write(padLeft("FANOUTS:", 11));
		if (fanoutReturnTypes == null || fanoutReturnTypes.isEmpty())
			t.write(" --");
		else {
			String indent = " ";
			for (ReturnTypeForFanout rtff : fanoutReturnTypes) {
				String entryIdFmted = padRight(rtff.getEntryId().toString2(), 25);
				t.write(indent+entryIdFmted + rtff.getReturnTypeENUM()+ "\r\n");
				if (indent.equals(" "))
					indent = "            ";
				
			}
		}
	}
	//TODO: consider moving this to FileWraper so it can print lists with new lines.

}
