package com.raygroupintl.m.token;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TBase;
import com.raygroupintl.m.cmdtree.EntryTag;
import com.raygroupintl.m.cmdtree.Line;
import com.raygroupintl.m.cmdtree.Routine;
import com.raygroupintl.m.struct.Fanout;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.m.struct.RoutineFanouts;
import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.struct.MLocationedError;

public class TRoutine extends TBase implements NodeFactory {
	private class TLineIterator implements Iterator<TLine> {
		private Routine routine = new Routine(TRoutine.this.name);
		int minAllowedLevel;
				
		private EntryTag entryTag;
		private int entryTagIndex;
		private int containerIndex;
				
		public Routine getNode() {
			while (this.containerIndex < TRoutine.this.lines.size()) {
				this.next();
			}
			return this.routine;
		}
		
		@Override
	    public boolean hasNext() {
			if (this.containerIndex < TRoutine.this.lines.size()) {
				TLine next = TRoutine.this.lines.get(this.containerIndex);
				int level = next.getLevel();
				return level >= this.minAllowedLevel;
			}
			return false;
	    }
			
		@Override
		public TLine next() throws NoSuchElementException {
			if (! hasNext()) {
				throw new NoSuchElementException();
			}
			TLine line = TRoutine.this.lines.get(this.containerIndex);
			++this.containerIndex;
			String tag = line.getTag();
			if ((tag != null) || (entryTag == null)) {
				if (tag == null) tag = "";
				this.entryTag = new EntryTag(tag);
				this.entryTagIndex = 0;
				this.routine.add(entryTag);
			}
			
			int currentMinAllowedLevel = this.minAllowedLevel;
			++this.minAllowedLevel;
			Line node = line.getNode(this.entryTagIndex, this);
			this.minAllowedLevel = currentMinAllowedLevel;
			
			this.entryTag.add(node);
			return line;
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private String name;
	private List<TLine> lines = new ArrayList<TLine>();
	private List<LineLocation> locations;
	
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
		List<MLocationedError> result = new ArrayList<MLocationedError>();
		List<LineLocation> locations = this.getLineLocations();
		for (int i=0; i<this.lines.size(); ++i) {
			TLine line = this.lines.get(i);
			LineLocation location = locations.get(i);
			if ((exemptions == null) || (! exemptions.contains(location))) {
				List<MError> errors = line.getErrors();
				if ((errors != null) && (errors.size() > 0)) {
					for (MError error : errors) {
						MLocationedError p = new MLocationedError(error, location);
						result.add(p);
					}
				}
			}
		}		
		return result;
	}	
	
	public RoutineFanouts getFanouts() {
		RoutineFanouts result = new RoutineFanouts(this.name);
		List<LineLocation> locations = this.getLineLocations();
		for (int i=0; i<this.lines.size(); ++i) {
			TLine line = this.lines.get(i);
			LineLocation location = locations.get(i);
			List<Fanout> fanouts = line.getFanouts();
			result.add(location, fanouts);
		}		
		return result;
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
	
	private List<LineLocation> buildLocations() {
		List<LineLocation> result = new ArrayList<LineLocation>();
		String lastTag = this.name;
		int index = 0;
		for (TLine line : this.lines) {
			String tag = line.getTag();
			if (tag != null) {
				lastTag = tag;
				index = 0;
			}
			LineLocation location = new LineLocation(lastTag, index);
			result.add(location);
			++index;
		}		
		return result;
	}
	
	private List<LineLocation> getLineLocations() {
		if ((this.locations == null) || (this.locations.size() != this.lines.size())) {
			this.locations = this.buildLocations();
		}
		return this.locations;
	}
	
	@Override
	public Routine getNode() {
		TLineIterator it = this.new TLineIterator();
		return it.getNode();
	}
}
