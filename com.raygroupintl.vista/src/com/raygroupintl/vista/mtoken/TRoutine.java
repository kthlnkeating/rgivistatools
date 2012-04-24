package com.raygroupintl.vista.mtoken;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.raygroupintl.bnf.TBase;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.m.struct.RoutineFanouts;
import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.struct.MLocationedError;

public class TRoutine extends TBase {
	private String name;
	private List<TLine> lines = new ArrayList<TLine>();
	
	public TRoutine(String name) {
		this.name = name;
	}
	
	public String getName() {
		if (this.name.endsWith(".m")) {
			return this.name.substring(0, this.name.length()-2);
		} else {
			return this.name;
		}
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
	public List<MError> getErrors() {
		List<MError> result = null;
		for (TLine line : this.lines) {
			List<MError> lerr = line.getErrors();
			if (lerr != null) {
				if (result == null) {
					result = new ArrayList<MError>();	
				}				
				result.addAll(lerr);
			}
		}
		return result;
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

	public List<MLocationedError> getErrors(Set<LineLocation> exemptions) throws IOException {
		String lastTag = this.name;
		int index = 0;
		List<MLocationedError> result = new ArrayList<MLocationedError>();
		for (TLine line : this.lines) {
			String tag = line.getTag();
			if (tag != null) {
				lastTag = tag;
				index = 0;
			}
			LineLocation location = new LineLocation(lastTag, index);
			if ((exemptions == null) || (! exemptions.contains(location))) {
				List<MError> errors = line.getErrors();
				if ((errors != null) && (errors.size() > 0)) {
					for (MError error : errors) {
						MLocationedError p = new MLocationedError(error, location);
						result.add(p);
					}
				}
			}
			++index;
		}		
		return result;
	}	
	
	public RoutineFanouts getFanouts() {
		return null;
	}
	
	public void write(Path path) throws IOException {
		List<String> fileLines = new ArrayList<String>();
		for (IToken line : this.lines) {
			if (line == null) {
				fileLines.add("");
			} else {
				String fileLine = line.getStringValue();
				fileLines.add(fileLine);
			}
		}
		Files.write(path, fileLines, StandardCharsets.UTF_8);
	}
}
