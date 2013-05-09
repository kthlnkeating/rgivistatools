package com.raygroupintl.m.token;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.raygroupintl.m.parsetree.Label;
import com.raygroupintl.m.parsetree.ErrorNode;
import com.raygroupintl.m.parsetree.InnerEntry;
import com.raygroupintl.m.parsetree.InnerEntryList;
import com.raygroupintl.m.parsetree.Line;
import com.raygroupintl.m.parsetree.NodeList;
import com.raygroupintl.m.parsetree.Entry;
import com.raygroupintl.m.parsetree.EntryList;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.struct.MError;
import com.raygroupintl.m.struct.MRefactorSettings;
import com.raygroupintl.parser.TextPiece;
import com.raygroupintl.parser.Token;

public class MRoutine implements MToken {
	private String name;
	private List<MLine> lines = new ArrayList<MLine>();
	
	public MRoutine(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void add(MLine line) {
		this.lines.add(line);
	}
	
	public List<MLine> asList() {
		return this.lines;
	}
	
	@Override
	public TextPiece toValue() {
		TextPiece result = new TextPiece();
		TextPiece eol = new TextPiece(getEOL());
		for (MLine line : this.lines) {
			result.add(line.toValue());
			result.add(eol);
		}
		return result;
	}
	
	@Override
	public void refactor(MRefactorSettings settings) {		
		for (MLine line : this.lines) {
			line.refactor(settings);
		}	
	}
	
	public static String getEOL() {
		String eol = System.getProperty("line.separator");
		if (eol == null) {
			eol = "\n";
		}
		return eol;		
	}

	public void write(OutputStream os) throws IOException {
		String seperator = getEOL();
		for (MLine line : this.lines) {
			String lineAsString = line.toValue().toString(); 
			os.write(lineAsString.getBytes());
			os.write(seperator.getBytes());
		}		
	}

	public void write(Path path) throws IOException {
		List<String> fileLines = new ArrayList<String>();
		for (Token line : this.lines) {
			if (line == null) {
				fileLines.add("");
			} else {
				String fileLine = line.toValue().toString();
				fileLines.add(fileLine);
			}
		}
		Files.write(path, fileLines, StandardCharsets.UTF_8);
	}
	
	@Override
	public Routine getNode() {
		if ((this.lines == null) || (this.lines.size() == 0)) {
			ErrorNode errorNode = new ErrorNode(MError.ERR_NO_LINES);
			return new Routine(this.name, errorNode);
		}

		Routine routine = new Routine(this.name);
		EntryList routineEntryList = new EntryList();
		routine.setEntryList(routineEntryList);
		
		NodeList<Label> entryList = routineEntryList;
		
		int level = 0;
		Line lineNode = null;
		Label entry = null;
		Stack<NodeList<Label>> entryLists = null;
		
		for (int i=0; i<this.lines.size(); ++i) {
			MLine line = this.lines.get(i);
			ErrorNode errorNode = null;
			
			int lineLevel = line.getLevel();
			if (lineLevel > level + 1) {
				errorNode = new ErrorNode(MError.ERR_BLOCK_STRUCTURE);
			}

			String tag = line.getTag();
			if (errorNode == null) {
				if (lineLevel < level) {
					for (int j=level; j>lineLevel; --j) {
						entryList = entryLists.pop();
					}
					entry = entryList.getLastNode();
					level = lineLevel;
				} else if (lineLevel > level) {
					if (lineNode == null) {
						errorNode = new ErrorNode(MError.ERR_BLOCK_STRUCTURE);
						return new Routine(this.name, errorNode);					
					}
					InnerEntryList newEntryList = new InnerEntryList();
					if (lineNode.setEntryList(newEntryList)) {
						if (entryLists == null) entryLists = new Stack<>();
						entryLists.push(entryList);
						entryList = newEntryList;
						entry = null;
						if (tag == null) tag = ":"  + String.valueOf(i);
						level = lineLevel;				
					} else {
						errorNode = new ErrorNode(MError.ERR_BLOCK_STRUCTURE);
					}
				}	
			}
			
			if ((tag != null) || (entry == null)) {
				if (tag == null) tag = "~";
				Label newEntry = (level == 0) ? new Entry(tag, this.name, line.getParameters()) : new InnerEntry(tag, this.name, line.getParameters());	 			
				if ((entry != null) && !(entry.isClosed())) {
					entry.setContinuationEntry(newEntry);
				}
				entry = newEntry;
				entryList.add(entry);
			}

			lineNode = (errorNode == null) ? line.getNode() : line.getAsErrorNode(errorNode);
			entry.add(lineNode);
		}
		
		return routine;
	}
	
	public int getNumSubNodes() {
		return 0;
	}
}
