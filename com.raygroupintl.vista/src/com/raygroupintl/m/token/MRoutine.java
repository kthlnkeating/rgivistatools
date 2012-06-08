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
		Routine routineNode = new Routine(this.name);
		EntryTag entryTagNode = null;
		int index = 0;
		for (MLine line : this.lines) {
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
