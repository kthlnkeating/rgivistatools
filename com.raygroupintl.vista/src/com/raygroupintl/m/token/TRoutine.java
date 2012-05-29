package com.raygroupintl.m.token;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.m.parsetree.EntryTag;
import com.raygroupintl.m.parsetree.Line;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.parser.Token;

public class TRoutine implements MToken {
	private String name;
	private List<TLine> lines = new ArrayList<TLine>();
	
	public TRoutine(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void add(TLine line) {
		this.lines.add(line);
	}
	
	public List<TLine> asList() {
		return this.lines;
	}
	
	@Override
	public String getStringValue() {
		StringBuilder sb = new StringBuilder();
		for (TLine line : this.lines) {
			sb.append(line.getStringValue());
			//sb.append('\n');
		}
		return sb.toString();
	}
	
	@Override
	public int getStringSize() {
		String v = this.getStringValue();
		return v.length();
	}
	
	@Override
	public void beautify() {		
		for (TLine line : this.lines) {
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
		for (TLine line : this.lines) {
			String lineAsString = line.getStringValue(); 
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
				String fileLine = line.getStringValue();
				fileLines.add(fileLine);
			}
		}
		Files.write(path, fileLines, StandardCharsets.UTF_8);
	}
	
	@Override
	public Routine getNode() {
		Routine routineNode = new Routine(this.name);
		EntryTag entryTagNode = null;
		int index = 0;
		for (TLine line : this.lines) {
			Line lineNode = line.getNode();
			String tag = line.getTag();
			if ((tag != null) || (entryTagNode == null)) {
				if (tag == null) tag = "";
				entryTagNode = new EntryTag(tag, this.name, index);
				routineNode.add(entryTagNode);
			}
			entryTagNode.add(lineNode);
			++index;
		}
		return routineNode;
	}
}
