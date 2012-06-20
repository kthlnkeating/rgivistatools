package com.raygroupintl.m.token;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.raygroupintl.m.parsetree.EntryList;
import com.raygroupintl.m.parsetree.Entry;
import com.raygroupintl.m.parsetree.ErrorNode;
import com.raygroupintl.m.parsetree.Line;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.struct.MError;
import com.raygroupintl.parser.StringPiece;
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
	public StringPiece toValue() {
		StringPiece result = new StringPiece();
		for (MLine line : this.lines) {
			result.add(line.toValue());
		}
		return result;
	}
	
	@Override
	public List<Token> toList() {
		List<Token> result = new ArrayList<Token>();
		for (MLine line : this.lines) {
			result.add(line);
		}
		return result;
	}
	
	@Override
	public void beautify() {		
		for (MLine line : this.lines) {
			line.beautify();
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
		EntryList entryList = new EntryList();
		routine.setEntryList(entryList);
				
		int index = 0;
		int level = 0;
		Line lineNode = null;
		Entry entry = null;
		Stack<EntryList> entryLists = null;
		
		for (int i=0; i<this.lines.size(); ++i) {
			MLine line = this.lines.get(i);
			
			int lineLevel = line.getLevel();
			if (lineLevel > level + 1) {
				ErrorNode errorNode = new ErrorNode(MError.ERR_BLOCK_STRUCTURE);
				return new Routine(this.name, errorNode);
			}

			if (lineLevel < level) {
				for (int j=level; j>lineLevel; --j) {
					entryList = entryLists.pop();
				}
				entry = entryList.getLastNode();
				level = lineLevel;
			} else if (lineLevel > level) {
				if (lineNode == null) {
					ErrorNode errorNode = new ErrorNode(MError.ERR_BLOCK_STRUCTURE);
					return new Routine(this.name, errorNode);					
				}
				if (entryLists == null) entryLists = new Stack<>();
				entryLists.push(entryList);
				entryList = new EntryList();
				if (! lineNode.setEntryList(entryList)) {
					ErrorNode errorNode = new ErrorNode(MError.ERR_BLOCK_STRUCTURE);
					return new Routine(this.name, errorNode);					
				}
				entry = null;
				level = lineLevel;
			}			
			String tag = line.getTag();
			if ((tag != null) || (entry == null)) {
				entry = new Entry(tag == null ? "" : tag, this.name, index, line.getParameters());
				entryList.add(entry);
			}

			lineNode = line.getNode();
			entry.add(lineNode);
			++index;
		}
		
		return routine;
	}
}
