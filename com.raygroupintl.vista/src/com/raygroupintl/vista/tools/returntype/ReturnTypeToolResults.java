package com.raygroupintl.vista.tools.returntype;

import java.util.LinkedList;

import com.raygroupintl.m.struct.EntryFanoutInfo;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.vista.tools.fnds.ToolResult;

public class ReturnTypeToolResults implements ToolResult {
	
	private String resultName;
	private LinkedList<EntryFanoutInfo> inResults;
	
	public ReturnTypeToolResults(String resultName, LinkedList<EntryFanoutInfo> inResults) {
		this.inResults = inResults;
		this.resultName = resultName;
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
		t.writeEOL(" " + resultName);
		
		boolean allValid = true;
		for (EntryFanoutInfo efi : inResults) {
			if (!efi.isValid()) {
				allValid = false;
				break;
			}
		}
		
		t.write(padLeft("INVALID FANOUTS:", 19));
		if (allValid)
			t.write(" --");
		else {
			String indent = " ";
			for (EntryFanoutInfo efi : inResults) { // lineLocation - fanoutName - calltype - return result
				if (efi.isValid())
					continue;
				
				String lineLocFmted = padRight(efi.getLineLocation().toString(), 17);
				if (efi.isFanoutExists()) {
					t.write(indent+lineLocFmted+ ": " + efi.getFanoutType() + " - " + efi.getFanoutTo().toString2()+ " - " +efi.getReturnType()+ "\r\n");
				} else {
					t.write(indent+lineLocFmted+ ": '" + efi.getFanoutTo() + "' Fanout could not be located" );
				}
				
				if (indent.equals(" "))
					indent = "                    ";
				
			}
		}
	}
	//TODO: consider moving this to FileWraper so it can print lists with new lines.

}
